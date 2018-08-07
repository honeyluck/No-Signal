package com.nosignal.mod.util.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(Side.CLIENT)
public class LimbManipulationUtil {
    private static Field textureOffsetXField = ModelRenderer.class.getDeclaredFields()[2];
    private static Field textureOffsetYField = ModelRenderer.class.getDeclaredFields()[3];

    public static LimbManipulator getLimbManipulator(RenderPlayer renderPlayer, Limb limb) {
        LimbManipulator manipulator = new LimbManipulator();
        try {
            textureOffsetXField.setAccessible(true);
            textureOffsetYField.setAccessible(true);

            List<LayerRenderer<AbstractClientPlayer>> layerList = ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, 4);

            for (LayerRenderer<AbstractClientPlayer> layer : layerList)
                for (Field field : layer.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getType() == ModelBiped.class) {
                        ModelBiped model = (ModelBiped) field.get(layer);
                        ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
                        manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.rendererField));
                        if (limb == Limb.HEAD) {
                            modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
                            manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.secondaryRendererField));
                        }
                    } else if (field.getType() == ModelPlayer.class) {
                        ModelBiped model = (ModelBiped) field.get(layer);
                        ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
                        manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.rendererField));
                        modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
                        manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.secondaryRendererField));
                    }
                }

            ModelPlayer model = renderPlayer.getMainModel();

            ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);

            manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.rendererField));
            modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
            manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.secondaryRendererField));

            textureOffsetXField.setAccessible(false);
            textureOffsetYField.setAccessible(false);
        } catch (IllegalAccessException ignored) {
        }
        return manipulator;
    }

    public static LimbManipulator getLimbManipulator(ModelPlayer model, Limb limb) {
        LimbManipulator manipulator = new LimbManipulator();
        try {
            textureOffsetXField.setAccessible(true);
            textureOffsetYField.setAccessible(true);

            ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);

            manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.rendererField));
            modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
            manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.secondaryRendererField));

            textureOffsetXField.setAccessible(false);
            textureOffsetYField.setAccessible(false);
        } catch (IllegalAccessException ignored) {
        }
        return manipulator;
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        @SuppressWarnings("rawtypes") RenderLivingBase renderer = (RenderLivingBase) Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(event.getEntityPlayer());
        List<LayerRenderer<AbstractClientPlayer>> layerList = ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderer, 4);
        try {
            for (LayerRenderer<AbstractClientPlayer> layer : layerList)
                for (Field field : layer.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getType() == ModelBiped.class) {
                        for (ModelRenderer modelRenderer : ((ModelBiped) field.get(layer)).boxList)
                            if (modelRenderer instanceof CustomModelRenderer)
                                ((CustomModelRenderer) modelRenderer).reset();
                    } else if (field.getType() == ModelPlayer.class)
                        for (ModelRenderer modelRenderer : ((ModelBiped) field.get(layer)).boxList)
                            if (modelRenderer instanceof CustomModelRenderer)
                                ((CustomModelRenderer) modelRenderer).reset();
                }
            for (ModelRenderer modelRenderer : event.getRenderer().getMainModel().boxList)
                if (modelRenderer instanceof CustomModelRenderer)
                    ((CustomModelRenderer) modelRenderer).reset();
        } catch (IllegalAccessException ignored) {
        }
    }
    //@formatter:on

    //@formatter:off
    public enum Limb {
        HEAD(ModelBiped.class.getDeclaredFields()[0], ModelBiped.class.getDeclaredFields()[1]),
        BODY(ModelBiped.class.getDeclaredFields()[2], ModelPlayer.class.getDeclaredFields()[4]),
        LEFT_ARM(ModelBiped.class.getDeclaredFields()[4], ModelPlayer.class.getDeclaredFields()[0]),
        RIGHT_ARM(ModelBiped.class.getDeclaredFields()[3], ModelPlayer.class.getDeclaredFields()[1]),
        LEFT_LEG(ModelBiped.class.getDeclaredFields()[6], ModelPlayer.class.getDeclaredFields()[2]),
        RIGHT_LEG(ModelBiped.class.getDeclaredFields()[5], ModelPlayer.class.getDeclaredFields()[3]);

        private Field rendererField, secondaryRendererField;

        Limb(Field rendererField, Field secondaryRendererField) {
            this.rendererField = rendererField;
            this.secondaryRendererField = secondaryRendererField;
        }
    }

    public static class LimbManipulator {

        private ArrayList<CustomModelRenderer> limbs = new ArrayList<>();

        public LimbManipulator setAngles(float x, float y, float z) {
            for (CustomModelRenderer limb : limbs)
                limb.setAngles(x, y, z);
            return this;
        }

        public LimbManipulator setOffsets(float x, float y, float z) {
            for (CustomModelRenderer limb : limbs)
                limb.setOffsets(x, y, z);
            return this;
        }
    }

    private static class CustomModelRenderer extends ModelRenderer {
        private float actualX, actualY, actualZ;
        private float offX, offY, offZ;
        private boolean changeAngles = false;
        private ModelBiped modelBiped;
        private ModelRenderer old;
        private Field f;

        private CustomModelRenderer(ModelBiped model, int texOffX, int texOffY, ModelRenderer old, Field field) throws IllegalAccessException {
            super(model, "");
            this.modelBiped = model;
            this.old = old;
            this.setTextureOffset(texOffX, texOffY);
            this.f = field;
            this.cubeList = old.cubeList;
            this.setRotationPoint(old.rotationPointX, old.rotationPointY, old.rotationPointZ);
            field.set(model, this);
        }

        @Override
        public void render(float scale) {
            if (this.changeAngles) {
                this.rotateAngleX = this.actualX;
                this.rotateAngleY = this.actualY;
                this.rotateAngleZ = this.actualZ;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(offX, offY, offZ);
            GlStateManager.rotate(this.rotateAngleX * 57.295776F, -1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, -1.0F);
            GlStateManager.translate(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
            super.render(scale);
            GlStateManager.popMatrix();
        }

        private void reset() {
            if (f != null)
                try {
                    this.f.set(modelBiped, this.old);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }

        private void setAnglesRadians(float x, float y, float z) {
            this.actualX = x;
            this.actualY = y;
            this.actualZ = z;
            this.changeAngles = true;
        }

        private void setAngles(float x, float y, float z) {
            this.setAnglesRadians((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
        }

        private void setOffsets(float x, float y, float z) {
            this.offX = x;
            this.offY = y;
            this.offZ = z;
        }
    }

}
