package com.ssomar.scoretestrecode.features.custom.cooldowns;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.configs.Message;
import com.ssomar.score.configs.messages.MessageMain;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.sobject.SObject;
import com.ssomar.score.sobject.sactivator.SActivator;
import com.ssomar.score.sobject.sactivator.cooldowns.Cooldown;
import com.ssomar.score.sobject.sactivator.cooldowns.CooldownsManager;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;
import com.ssomar.score.utils.placeholders.StringPlaceholder;
import com.ssomar.scoretestrecode.editor.NewGUIManager;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.custom.activators.activator.NewSActivator;
import com.ssomar.scoretestrecode.features.types.BooleanFeature;
import com.ssomar.scoretestrecode.features.types.ColoredStringFeature;
import com.ssomar.scoretestrecode.features.types.IntegerFeature;
import com.ssomar.scoretestrecode.sobject.NewSObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

@Getter@Setter
public class NewCooldownFeature extends FeatureWithHisOwnEditor<NewCooldownFeature, NewCooldownFeature, NewCooldownFeatureEditor, NewCooldownFeatureEditorManager> {

    /* Cooldowns / delay */
    private IntegerFeature cooldown;
    private ColoredStringFeature cooldownMessage;
    private BooleanFeature displayCooldownMessage;
    private BooleanFeature isCooldownInTicks;
    private BooleanFeature cancelEventIfInCooldown;
    private String cooldownId;
    private SPlugin sPlugin;
    private boolean enableCooldownForOp;

    public NewCooldownFeature(FeatureParentInterface parent, String name, String editorName, String[] editorDescription, Material editorMaterial, boolean requirePremium, SPlugin sPlugin, boolean enableCooldownForOp) {
        super(parent, name, editorName, editorDescription, editorMaterial, requirePremium);
        this.sPlugin = sPlugin;
        if(getParent() instanceof NewSActivator) {
            NewSActivator newSActivator = (NewSActivator) getParent();
            this.cooldownId = newSActivator.getParentObjectId()+":"+newSActivator.getId();
        }
        else this.cooldownId = UUID.randomUUID().toString();
        this.enableCooldownForOp = enableCooldownForOp;
        reset();
    }

    /**
     *
     * @param p The player
     * @param e The event
     * @param sp The placeholder associate to the event
     * @return True in No coooldown, false if its on cooldown
     */
    public boolean checkCooldown(Player p, Event e, StringPlaceholder sp, NewSObject sObject) {
        /* Check if the activator is in cooldown for the player or not  */
        if (!hasNoCDPerm(p, sObject)) {
            Optional<Cooldown> inCooldownOpt = CooldownsManager.getInstance().getCooldown(ExecutableItems.plugin, cooldownId, p.getUniqueId());
            if (inCooldownOpt.isPresent()) {
                if (this.displayCooldownMessage.getValue()) {
                    displayCooldownMessage(p, inCooldownOpt.get().getTimeLeft(), sp);
                }
                SActivator.cancelEvent(e, this.cancelEventIfInCooldown.getValue());
                return false;
            }
        }
        return true;
    }

    public void displayCooldownMessage(Player player, double timeLeft, StringPlaceholder sp){
        String message = cooldownMessage.getValue().get();
        if(message.isEmpty()){
            message = MessageMain.getInstance().getMessage(ExecutableItems.plugin, Message.TIME_LEFT);
        }
        sp.getTimePlch().setTimePlcHldr(timeLeft);
        message = sp.replacePlaceholder(message);
        player.sendMessage(StringConverter.coloredString(message));
    }

    /**
     *
     * @param p The player
     */
    public void addCooldown(Player p, @NotNull NewSObject sObject){
        if (!hasNoCDPerm(p, sObject) && this.cooldown.getValue().get() != 0) {
            Cooldown cooldown = new Cooldown(ExecutableItems.plugin, cooldownId, p.getUniqueId(), this.cooldown.getValue().get(), isCooldownInTicks.getValue(), System.currentTimeMillis(), false);
            CooldownsManager.getInstance().addCooldown(cooldown);
        }
    }

    public boolean hasNoCDPerm(Player p, NewSObject sObject){
        String id = sObject.getId();

        if(sPlugin.isLotOfWork()) return false;

        if(p.isOp() || p.hasPermission("*")) {
            return !enableCooldownForOp;
        }
        else{
            return p.hasPermission(sPlugin.getName()+".nocd." + id) ||
                    p.hasPermission(sPlugin.getShortName().toLowerCase()+".nocd." + id) ||
                    p.hasPermission(sPlugin.getName()+".nocd.*") ||
                    p.hasPermission(sPlugin.getShortName().toLowerCase()+".nocd.*");
        }
    }


    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        errors.addAll(cooldown.load(plugin, config, isPremiumLoading));
        errors.addAll(displayCooldownMessage.load(plugin, config, isPremiumLoading));
        errors.addAll(cancelEventIfInCooldown.load(plugin, config, isPremiumLoading));
        errors.addAll(cooldownMessage.load(plugin, config, isPremiumLoading));
        errors.addAll(isCooldownInTicks.load(plugin, config, isPremiumLoading));
        return errors;
    }

    @Override
    public NewCooldownFeature clone() {
        NewCooldownFeature clone = new NewCooldownFeature(getParent(), getName(), getEditorName(), getEditorDescription(), getEditorMaterial(), isRequirePremium(), getSPlugin(), isEnableCooldownForOp());
        clone.setCooldown(cooldown.clone());
        clone.setDisplayCooldownMessage(displayCooldownMessage.clone());
        clone.setCancelEventIfInCooldown(cancelEventIfInCooldown.clone());
        clone.setCooldownMessage(cooldownMessage.clone());
        clone.setIsCooldownInTicks(isCooldownInTicks.clone());
        return clone;
    }

    @Override
    public void save(ConfigurationSection config) {
        this.cooldown.save(config);
        this.isCooldownInTicks.save(config);
        this.cooldownMessage.save(config);
        this.displayCooldownMessage.save(config);
        this.cancelEventIfInCooldown.save(config);
    }

    @Override
    public NewCooldownFeature getValue() {
        return this;
    }

    @Override
    public NewCooldownFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 5];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 5] = gui.CLICK_HERE_TO_CHANGE;

        finalDescription[finalDescription.length - 4] = "&7Cooldown: &e" + getCooldown().getValue().get();

        if (isCooldownInTicks.getValue()) {
            finalDescription[finalDescription.length - 3] = "&7Cooldown in ticks: &a&l✔";
        }
        else {
            finalDescription[finalDescription.length - 3] = "&7Cooldown in ticks: &c&l✘";
        }
        if(displayCooldownMessage.getValue()) {
            finalDescription[finalDescription.length - 2] = "&7Display cooldown message: &a&l✔";
        }
        else {
            finalDescription[finalDescription.length - 2] = "&7Display cooldown message: &c&l✘";
        }
        if(cancelEventIfInCooldown.getValue()) {
            finalDescription[finalDescription.length - 1] = "&7Cancel event if in cooldown: &a&l✔";
        }
        else {
            finalDescription[finalDescription.length - 1] = "&7Cancel event if in cooldown: &c&l✘";
        }

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public void extractInfoFromParentEditor(NewGUIManager manager, Player player) {

    }

    @Override
    public void reset() {
        this.cooldown = new IntegerFeature(this, "cooldown", Optional.of(0), "Cooldown", new String[]{"&7&oThe cooldown"}, GUI.CLOCK, false);
        this.isCooldownInTicks = new BooleanFeature(this, "isCooldownInTicks", false, "Cooldown in ticks", new String[]{"&7&oIs the cooldown in ticks?"}, Material.LEVER, false, false);
        this.cooldownMessage = new ColoredStringFeature(this, "cooldownMsg", Optional.of("&cYou are in cooldown ! &7(&e%time_H%&6H &e%time_M%&6M &e%time_S%&6S&7)"), "Cooldown Message", new String[]{"&7&oThe cooldown message"}, GUI.WRITABLE_BOOK, false, false);
        this.displayCooldownMessage = new BooleanFeature(this, "displayCooldownMessage", true, "Display Cooldown Message", new String[]{"&7&oDisplay the cooldown message"}, Material.LEVER, false, false);
        this.cancelEventIfInCooldown = new BooleanFeature(this, "cancelEventIfInCooldown", false, "Cancel Event If In Cooldown", new String[]{"&7&oCancel the event if the player is in cooldown?"}, Material.LEVER, false, false);
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(Arrays.asList(cooldown, isCooldownInTicks, cooldownMessage, displayCooldownMessage, cancelEventIfInCooldown));
    }

    @Override
    public String getParentInfo() {
        return getParent().getParentInfo();
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return getParent().getConfigurationSection();
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for(FeatureInterface feature : getParent().getFeatures()) {
            if(feature instanceof NewCooldownFeature) {
                NewCooldownFeature coolodwn = (NewCooldownFeature) feature;
                coolodwn.setCooldown(this.cooldown);
                coolodwn.setIsCooldownInTicks(this.isCooldownInTicks);
                coolodwn.setCooldownMessage(this.cooldownMessage);
                coolodwn.setDisplayCooldownMessage(this.displayCooldownMessage);
                coolodwn.setCancelEventIfInCooldown(this.cancelEventIfInCooldown);
                break;
            }
        }
    }

    @Override
    public void openBackEditor(@NotNull Player player) {
        getParent().openEditor(player);
    }

    @Override
    public void openEditor(@NotNull Player player) {
        NewCooldownFeatureEditorManager.getInstance().startEditing(player, this);
    }
}
