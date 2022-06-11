package com.ssomar.scoretestrecode.features.custom.conditions.item.condition;

import com.ssomar.executableitems.executableitems.ExecutableItemObject;
import com.ssomar.score.conditions.condition.conditiontype.ConditionType;
import com.ssomar.score.conditions.condition.item.ItemCondition;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.score.utils.StringCalculation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class IfUsage extends ItemCondition<String, String> {


    public IfUsage() {
        super(ConditionType.NUMBER_CONDITION, "ifUsage", "If usage", new String[]{}, "", " &cThis item must have the valid usage to active the activator: &6%activator% &cof this item!");
    }

    @Override
    public boolean verifCondition(ItemStack itemStack, Optional<Player> playerOpt, SendMessage messageSender) {

        ExecutableItemObject executableItem = new ExecutableItemObject(itemStack);
        if(executableItem.isValid()){
            executableItem.loadExecutableItemInfos();
            if(isDefined()) {
                if(!StringCalculation.calculation(getAllCondition(messageSender.getSp()), executableItem.getUsage())) {
                    sendErrorMsg(playerOpt, messageSender);
                    return false;
                }
            }
        }

        return true;
    }
}
