package me.syz.freelook;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import me.syz.freelook.config.FreelookConfig;
import net.minecraft.client.Minecraft;

public class Freelook {
    public static final Freelook INSTANCE;
    public static final Minecraft mc = Minecraft.getMinecraft();

    static {
        INSTANCE = new Freelook();
    }

    public boolean perspectiveToggled = false;
    public boolean prevState = false;
    public float cameraYaw = 0.0f;
    public float cameraPitch = 0.0f;
    private int previousPerspective = 0;

    public void onPressed(boolean down) {
        if (FreelookMod.config.enabled) {
            if (down) {
                cameraYaw = mc.thePlayer.rotationYaw;
                cameraPitch = mc.thePlayer.rotationPitch;

                if (perspectiveToggled) {
                    resetPerspective();
                } else {
                    enterPerspective();
                }

                mc.renderGlobal.setDisplayListEntitiesDirty();
            } else if (FreelookConfig.hold) {
                resetPerspective();
            }
        } else if (perspectiveToggled) {
            resetPerspective();
        }
    }

    public void enterPerspective() {
        perspectiveToggled = true;

        previousPerspective = mc.gameSettings.thirdPersonView;

        mc.gameSettings.thirdPersonView = FreelookConfig.perspective;
    }

    public void resetPerspective() {
        perspectiveToggled = false;

        mc.gameSettings.thirdPersonView = previousPerspective;

        mc.renderGlobal.setDisplayListEntitiesDirty();
    }

    public boolean overrideMouse() {
        if (mc.inGameHasFocus) {
            if (!perspectiveToggled) return true;

            if (HypixelUtils.INSTANCE.isHypixel() || FreelookConfig.snaplook) {
                cameraYaw = mc.thePlayer.rotationYaw;
                cameraPitch = mc.thePlayer.rotationPitch;
                return true;
            }

            mc.mouseHelper.mouseXYChange();

            if (FreelookConfig.yaw) handleYaw();
            if (FreelookConfig.pitch) handlePitch();

            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
        return false;
    }

    public void handleYaw() {
        float sensitivity = calculateSensitivity();
        float yaw = mc.mouseHelper.deltaX * sensitivity;

        if (FreelookConfig.invertYaw) yaw = -yaw;

        cameraYaw += yaw * 0.15f;
    }

    public void handlePitch() {
        float sensitivity = calculateSensitivity();
        float pitch = mc.mouseHelper.deltaY * sensitivity;

        if (FreelookConfig.invertPitch) pitch = -pitch;

        cameraPitch += pitch * 0.15f;

        if (FreelookConfig.lockPitch) cameraPitch = Math.max(-90.0f, Math.min(cameraPitch, 90.0f));
    }

    public float calculateSensitivity() {
        float sensitivity = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return sensitivity * sensitivity * sensitivity * 8.0f;
    }
}
