package com.ssomar.score.features.custom.conditions.entity.condition;

import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionFeature;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionRequest;
import com.ssomar.score.features.types.BooleanFeature;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class IfNotFromSpawner extends EntityConditionFeature<BooleanFeature, IfNotFromSpawner> {

    public IfNotFromSpawner(FeatureParentInterface parent) {
        super(parent, "ifNotFromSpawner", "If not from spawner", new String[]{}, Material.ANVIL, false);
    }

    @Override
    public void subReset() {
        setCondition(new BooleanFeature(getParent(), "ifNotFromSpawner", false, "If not from spawner", new String[]{}, Material.ANVIL, false, true));
    }

    @Override
    public boolean hasCondition() {
        return getCondition().getValue();
    }

    @Override
    public IfNotFromSpawner getNewInstance(FeatureParentInterface parent) {
        return new IfNotFromSpawner(parent);
    }

    @Override
    public boolean verifCondition(EntityConditionRequest request) {
        Entity entity = request.getEntity();
        if (hasCondition() && entity.hasMetadata("fromSpawner")) {
            runInvalidCondition(request);
            return false;
        }

        return true;
    }

    @Override
    public IfNotFromSpawner getValue() {
        return this;
    }
}
