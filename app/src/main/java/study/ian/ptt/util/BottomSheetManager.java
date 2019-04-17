package study.ian.ptt.util;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.HashSet;
import java.util.Set;

public class BottomSheetManager {
    private final Set<BottomSheetBehavior> behaviorSet = new HashSet<>();

    public void addToSet(BottomSheetBehavior behavior) {
        behaviorSet.add(behavior);
    }

    public void expandSheet(BottomSheetBehavior behavior) {
        for (BottomSheetBehavior b : behaviorSet) {
            if (b != behavior) {
                collapseSheet(b);
            }
        }
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapseSheet(BottomSheetBehavior behavior) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
