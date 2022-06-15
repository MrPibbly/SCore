package com.ssomar.scoretestrecode.features.types;

import com.ssomar.score.menu.EditorCreator;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;
import com.ssomar.scoretestrecode.editor.NewGUIManager;
import com.ssomar.scoretestrecode.editor.Suggestion;
import com.ssomar.scoretestrecode.features.FeatureAbstract;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureRequireSubTextEditorInEditor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Getter
@Setter
public class ListEffectAndLevelFeature extends FeatureAbstract<Map<PotionEffectType, Integer>, ListEffectAndLevelFeature> implements FeatureRequireSubTextEditorInEditor {

    private Map<PotionEffectType, Integer> value;
    private Map<PotionEffectType, Integer> defaultValue;

    public ListEffectAndLevelFeature(FeatureParentInterface parent, String name, Map<PotionEffectType, Integer> defaultValue, String editorName, String[] editorDescription, Material editorMaterial, boolean requirePremium) {
        super(parent, name, editorName, editorDescription, editorMaterial, requirePremium);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        reset();
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        value = transformEffects(config.getStringList(this.getName()), errors);
        return errors;
    }

    public Map<PotionEffectType, Integer> transformEffects(List<String> enchantsConfig, List<String> errorList){
        Map<PotionEffectType, Integer> result = new HashMap<>();
        for(String s : enchantsConfig){
            PotionEffectType effect;
            int level;
            String [] decomp;

            boolean error = false;
            if (s.contains(":")) {
                decomp = s.split(":");
                try {
                    effect = PotionEffectType.getByName(decomp[0]);
                    if(effect == null){
                        error = true;
                    }
                    else {
                        level = Integer.parseInt(decomp[1]);
                        result.put(effect, level);
                    }
                } catch (Exception e) {
                    error = true;
                }
                if (error) errorList.add("&cERROR, Couldn't load the Effect with level value of " + this.getName() + " from config, value: " + s+ " &7&o"+getParent().getParentInfo()+" &6>> correct form > EFFECT:LEVEL  example> SPEED:1 !");
            }
        }
        return result;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(this.getName(), this.getCurrentValues());
    }

    @Override
    public Map<PotionEffectType, Integer> getValue() {
        return value;
    }

    @Override
    public ListEffectAndLevelFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = "&7actually: ";

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {
        gui.updateConditionList(getEditorName(), this.getCurrentValues(), "&cEMPTY");
    }

    @Override
    public void extractInfoFromParentEditor(NewGUIManager manager, Player player) {
        List<String> preResult = ((GUI) manager.getCache().get(player)).getConditionListWithColor(getEditorName(), "&cEMPTY");
         value = transformEffects(preResult, new ArrayList<>());
    }

    @Override
    public ListEffectAndLevelFeature clone() {
        ListEffectAndLevelFeature clone = new ListEffectAndLevelFeature(getParent(), this.getName(), getDefaultValue(), getEditorName(), getEditorDescription(), getEditorMaterial(), isRequirePremium());
        clone.setValue(getValue());
        return clone;
    }

    @Override
    public void reset() {
        this.value = defaultValue;
    }

    @Override
    public Optional<String> verifyMessageReceived(String message) {
        String s = StringConverter.decoloredString(message);
        String [] decomp;
        Enchantment enchant;
        int level;
        boolean error = false;
        if (s.contains(":")) {
            decomp = s.split(":");
            try {
                enchant = Enchantment.getByName(decomp[0]);
                if(enchant == null){
                    error = true;
                }
                else {
                    Integer.parseInt(decomp[1]);
                }
            } catch (Exception e) {
                error = true;
            }
            if(error) return Optional.of("&4&l[ERROR] &cThe message you entered &8(&7"+s+"&8)&c is not an effect with level &6>> correct form > EFFECT:LEVEL  example> SPEED:1 !");
        }
        return Optional.empty();
    }

    @Override
    public List<String> getCurrentValues() {
        List<String> result = new ArrayList<>();
        for(Map.Entry<PotionEffectType, Integer> entry : value.entrySet()){
            result.add(entry.getKey().toString() + ":" + entry.getValue());
        }
        return result;
    }

    @Override
    public List<Suggestion> getSuggestions() {
        SortedMap<String, Suggestion> map = new TreeMap<String, Suggestion>();
        for(PotionEffectType effect : PotionEffectType.values()){
            map.put(effect.getName()+"", new Suggestion(effect.getName()+":1", "&6["+"&e"+effect.getName()+"&6]", "&7Add &e"+effect.getName()));
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public void finishEditInSubEditor(Player editor, NewGUIManager manager) {
        List<String> preResult = (List<String>) manager.currentWriting.get(editor);
        for(int i = 0; i < preResult.size(); i++) {
            preResult.set(i, StringConverter.decoloredString(preResult.get(i)));
        }
        value = transformEffects(preResult, new ArrayList<>());
        manager.requestWriting.remove(editor);
        manager.activeTextEditor.remove(editor);
        updateItemParentEditor((GUI) manager.getCache().get(editor));
    }


    @Override
    public void sendBeforeTextEditor(Player playerEditor, NewGUIManager manager) {
        List<String> beforeMenu = new ArrayList<>();
        beforeMenu.add("&7➤ Your custom " + getEditorName() + ":");

        HashMap<String, String> suggestions = new HashMap<>();

        EditorCreator editor = new EditorCreator(beforeMenu, (List<String>) manager.currentWriting.get(playerEditor), getEditorName() + ":", true, true, true, true,
                true, true, true, "", suggestions);
        editor.generateTheMenuAndSendIt(playerEditor);
    }
}
