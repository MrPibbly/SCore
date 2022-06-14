package com.ssomar.scoretestrecode.features.custom.conditions.player.condition;

import com.ssomar.score.conditions.condition.conditiontype.ConditionType;
import com.ssomar.score.conditions.condition.player.PlayerCondition;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.custom.conditions.player.PlayerConditionFeature;
import com.ssomar.scoretestrecode.features.types.ListMaterialFeature;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IfIsNotOnTheBlock extends PlayerConditionFeature<ListMaterialFeature, IfIsNotOnTheBlock> {

    public IfIsNotOnTheBlock(FeatureParentInterface parent) {
        super(parent, "ifIsNotOnTheBlock", "If is not on the block", new String[]{}, Material.ANVIL, false);
    }

    @Override
    public boolean verifCondition(Player player, Optional<Player> playerOpt, SendMessage messageSender, Event event) {
        if (hasCondition()) {
            Location pLoc = player.getLocation();
            pLoc.subtract(0, 0.1, 0);

            Block block = pLoc.getBlock();
            Material type = block.getType();

            if (getCondition().getValue().contains(type)) {
                sendErrorMsg(playerOpt, messageSender);
                cancelEvent(event);
                return false;
            }
        }
        return true;
    }

    @Override
    public IfIsNotOnTheBlock getValue() {
        return  this;
    }

    @Override
    public void subReset() {
        setCondition(new ListMaterialFeature(this, "ifIsNotOnTheBlock", new ArrayList<>(), "If is not on the block", new String[]{}, Material.ANVIL, false));
    }

    @Override
    public boolean hasCondition() {
        return  getCondition().getValue().size() > 0;
    }

    @Override
    public IfIsNotOnTheBlock getNewInstance() {
        return  new IfIsNotOnTheBlock(getParent());
    }
}
