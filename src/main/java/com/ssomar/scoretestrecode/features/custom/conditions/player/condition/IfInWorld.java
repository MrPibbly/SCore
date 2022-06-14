package com.ssomar.scoretestrecode.features.custom.conditions.player.condition;

import com.ssomar.score.utils.SendMessage;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.custom.conditions.player.PlayerConditionFeature;
import com.ssomar.scoretestrecode.features.types.ListWorldFeature;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Optional;

public class IfInWorld extends PlayerConditionFeature<ListWorldFeature, IfInWorld> {

    public IfInWorld(FeatureParentInterface parent) {
        super(parent, "ifInWorld", "If in world", new String[]{}, Material.ANVIL, false);
    }

    @Override
    public boolean verifCondition(Player player, Optional<Player> playerOpt, SendMessage messageSender, Event event) {
        if (hasCondition()) {
            boolean notValid = true;
            for (String s : getCondition().getValue()) {
                if (player.getWorld().getName().equalsIgnoreCase(s)) {
                    notValid = false;
                    break;
                }
            }
            if (notValid) {
                sendErrorMsg(playerOpt, messageSender);
                cancelEvent(event);
                return false;
            }
        }
        return true;
    }

    @Override
    public IfInWorld getValue() {
        return this;
    }

    @Override
    public void subReset() {
        setCondition(new ListWorldFeature(getParent(), "ifInWorld", new ArrayList<>(), "If in world", new String[]{}, Material.ANVIL, false));
    }

    @Override
    public boolean hasCondition() {
        return getCondition().getValue().size() > 0;
    }

    @Override
    public IfInWorld getNewInstance() {
        return new IfInWorld(getParent());
    }
}
