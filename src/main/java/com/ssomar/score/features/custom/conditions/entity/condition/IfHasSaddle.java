package com.ssomar.score.features.custom.conditions.entity.condition;

import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionFeature;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionRequest;
import com.ssomar.score.features.types.BooleanFeature;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.InventoryHolder;

public class IfHasSaddle extends EntityConditionFeature<BooleanFeature, IfHasSaddle> {

    public IfHasSaddle(FeatureParentInterface parent) {
        super(parent, "ifHasSaddle", "If has Saddle", new String[]{}, Material.ANVIL, false);
    }

    @Override
    public void subReset() {
        setCondition(new BooleanFeature(getParent(), "ifHasSaddle", false, "If has Saddle", new String[]{}, Material.ANVIL, false, true));
    }

    @Override
    public boolean hasCondition() {
        return getCondition().getValue();
    }

    @Override
    public IfHasSaddle getNewInstance(FeatureParentInterface parent) {
        return new IfHasSaddle(parent);
    }

    @Override
    public boolean verifCondition(EntityConditionRequest request) {
        Entity entity = request.getEntity();
        if (hasCondition() && (!(entity instanceof InventoryHolder) || !(((InventoryHolder) entity).getInventory() instanceof AbstractHorseInventory) || ((AbstractHorseInventory) ((InventoryHolder) entity).getInventory()).getSaddle() == null)) {
            runInvalidCondition(request);
            return false;
        }

        return true;
    }

    @Override
    public IfHasSaddle getValue() {
        return this;
    }
}
