package me.syz.freelook.mixins;

import me.syz.freelook.Freelook;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ActiveRenderInfo.class)
public class ActiveRenderInfoMixin {
    @Redirect(method = "updateRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationPitch:F", opcode = Opcodes.GETFIELD))
    private static float modifyPitch(EntityPlayer player) {
        return Freelook.INSTANCE.perspectiveToggled ? Freelook.INSTANCE.cameraPitch : player.rotationPitch;
    }

    @Redirect(method = "updateRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationYaw:F", opcode = Opcodes.GETFIELD))
    private static float modifyYaw(EntityPlayer player) {
        return Freelook.INSTANCE.perspectiveToggled ? Freelook.INSTANCE.cameraYaw : player.rotationYaw;
    }
}