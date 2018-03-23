package com.gionee.gnif.file.constant;

/**
 * Created by yeqy on 2017/5/27.
 */
public enum UploadStep {
    STEP_ONE(1), STEP_TWO(2), STEP_THREE(3), STEP_FOUR(4), STEP_NULL(0);

    private Integer step;

    UploadStep(Integer step) {
        this.step = step;
    }

    public static UploadStep valueOf(int step) {
        switch (step) {
            case 1:
                return STEP_ONE;
            case 2:
                return STEP_TWO;
            case 3:
                return STEP_THREE;
            case 4:
                return STEP_FOUR;
            default:
                return STEP_NULL;
        }
    }

    public Integer getStep() {
        return step;
    }
}
