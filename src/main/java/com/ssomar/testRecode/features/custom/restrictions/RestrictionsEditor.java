package com.ssomar.testRecode.features.custom.restrictions;

import com.ssomar.score.menu.GUI;
import com.ssomar.testRecode.features.editor.FeatureEditorInterface;

public class RestrictionsEditor extends FeatureEditorInterface<Restrictions> {

    public Restrictions restrictions;

    public RestrictionsEditor(Restrictions dropFeatures) {
        super("&lRestrictions Editor", 5*9);
        this.restrictions = dropFeatures.clone();
        load();
    }

    @Override
    public void load() {
        int cpt = 0;
        for(RestrictionEnum restriction : RestrictionEnum.values()) {
            restrictions.get(restriction).initAndUpdateItemParentEditor(this, cpt);
            cpt++;
        }

        // Back
        createItem(RED, 	1, 36, GUI.BACK, false, false);

        // Reset menu
        createItem(ORANGE, 			1, 37, GUI.RESET, false, false, "", "&c&oClick here to reset");

        // Save menu
        createItem(GREEN, 1, 44, GUI.SAVE, false, false, "", "&a&oClick here to save");
    }

    @Override
    public Restrictions getParent() {
        return restrictions;
    }
}
