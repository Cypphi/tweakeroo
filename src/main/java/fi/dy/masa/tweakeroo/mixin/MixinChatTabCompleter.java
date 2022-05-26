package fi.dy.masa.tweakeroo.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat.ChatTabCompleter;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import fi.dy.masa.malilib.util.GameUtils;
import fi.dy.masa.malilib.util.wrap.EntityWrap;
import fi.dy.masa.tweakeroo.config.FeatureToggle;

@Mixin(ChatTabCompleter.class)
public abstract class MixinChatTabCompleter extends TabCompleter
{
    public MixinChatTabCompleter(GuiTextField textFieldIn, boolean hasTargetBlockIn)
    {
        super(textFieldIn, hasTargetBlockIn);
    }

    @Inject(method = "getTargetBlockPos", at = @At("RETURN"), cancellable = true)
    private void onGetTargetPos(CallbackInfoReturnable<BlockPos> cir)
    {
        if (FeatureToggle.TWEAK_TAB_COMPLETE_COORDINATE.getBooleanValue())
        {
            Minecraft mc = GameUtils.getClient();

            if (mc.player != null && (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK))
            {
                cir.setReturnValue(EntityWrap.getPlayerBlockPos());
                cir.cancel();
            }
        }
    }
}
