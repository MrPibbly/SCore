package com.ssomar.testRecode.features.types;

import com.ssomar.score.menu.GUI;
import com.ssomar.score.menu.GUIManager;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;
import com.ssomar.testRecode.features.FeatureAbstract;
import com.ssomar.testRecode.features.FeatureParentInterface;
import com.ssomar.testRecode.features.FeatureRequireOneMessageInEditor;
import com.ssomar.testRecode.menu.NewGUIManager;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ssomar.score.menu.conditions.RequestMessage.space;

@Getter
public class ColoredStringFeature extends FeatureAbstract<Optional<String>, ColoredStringFeature> implements FeatureRequireOneMessageInEditor {

    private Optional<String> value;

    public ColoredStringFeature(FeatureParentInterface parent, String name, String editorName, String [] editorDescription, Material editorMaterial) {
        super(parent, name, editorName, editorDescription, editorMaterial);
        this.value = Optional.empty();
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        String valueStr = config.getString(getName(), "");
        if(valueStr.isEmpty()) value = Optional.empty();
        else value = Optional.of(valueStr);
        return new ArrayList<>();
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(getName(), value);
    }

    @Override
    public Optional<String> getValue() {
        return value;
    }

    @Override
    public ColoredStringFeature initItemParentEditor(GUI gui, int slot) {
        String [] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = "&7actually: ";

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR+getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {
        if(value.isPresent()) gui.updateActually(getEditorName(), getValue().get(), true);
        else gui.updateActually(getEditorName(), "&cEMPTY STRING", true);
    }

    @Override
    public void extractInfoFromParentEditor(NewGUIManager manager, Player player) {
        String valueStr = ((GUI)manager.getCache().get(player)).getActuallyWithColor(getEditorName());
        if(valueStr.isEmpty()) value = Optional.empty();
        else value = Optional.of(valueStr);
    }

    @Override
    public ColoredStringFeature clone() {
        return new ColoredStringFeature(getParent(), getName(), getEditorName(), getEditorDescription(), getEditorMaterial());
    }

    @Override
    public void reset() {
        this.value = Optional.empty();
    }

    @Override
    public void askInEditor(Player editor, NewGUIManager manager) {
        manager.requestWriting.put(editor, getEditorName());
        editor.closeInventory();
        space(editor);

        TextComponent message = new TextComponent(StringConverter.coloredString("&a&l[Editor] &aEnter a string or &aedit &athe &aactual: "));

        TextComponent edit = new TextComponent(StringConverter.coloredString("&e&l[EDIT]"));
        edit.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, StringConverter.deconvertColor(((GUI)manager.getCache().get(editor)).getActuallyWithColor(getEditorName()))));
        edit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringConverter.coloredString("&eClick here to edit the current string")).create()));

        TextComponent newName = new TextComponent(StringConverter.coloredString("&a&l[NEW]"));
        newName.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "Type the new string here.."));
        newName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringConverter.coloredString("&aClick here to set new string")).create()));

        message.addExtra(new TextComponent(" "));
        message.addExtra(edit);
        message.addExtra(new TextComponent(" "));
        message.addExtra(newName);

        editor.spigot().sendMessage(message);
        space(editor);
    }

    @Override
    public Optional<String> verifyMessageReceived(String message) {
        return Optional.empty();
    }

    @Override
    public void finishEditInEditor(Player editor, NewGUIManager manager) {
        String valueStr = ((List<String>)manager.currentWriting.get(editor)).get(0);
        if(valueStr.isEmpty()) value = Optional.empty();
        else value = Optional.of(valueStr);
        updateItemParentEditor((GUI) manager.getCache().get(editor));
    }
}
