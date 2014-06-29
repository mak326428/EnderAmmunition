package enderamm.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelHMS extends ModelBase {
    // fields
    ModelRenderer SmallPlatform2;
    ModelRenderer MainPlatform;
    ModelRenderer SmallPlatform4;
    ModelRenderer SmallPlatform1;
    ModelRenderer UpperSmallPlatform2;
    ModelRenderer Pillar4;
    ModelRenderer Pillar1;
    ModelRenderer Pillar2;
    ModelRenderer Pillar3;
    ModelRenderer UpperSmallPlatform1;
    ModelRenderer SmallPlatform3;
    ModelRenderer UpperSmallPlatform3;
    ModelRenderer UpperSmallPlatform4;
    ModelRenderer UpperMainPlatform;
    ModelRenderer Glass1;
    ModelRenderer Glass2;
    ModelRenderer Glass3;
    ModelRenderer Glass4;
    ModelRenderer Glass5;
    ModelRenderer Glass6;
    ModelRenderer Glass7;
    ModelRenderer Glass8;

    public ModelHMS() {
        textureWidth = 64;
        textureHeight = 32;

        SmallPlatform2 = new ModelRenderer(this, 0, 11);
        SmallPlatform2.addBox(0F, 0F, 0F, 4, 1, 4);
        SmallPlatform2.setRotationPoint(-5F, 22F, 1F);
        SmallPlatform2.setTextureSize(64, 32);
        SmallPlatform2.mirror = true;
        setRotation(SmallPlatform2, 0F, 0F, 0F);
        MainPlatform = new ModelRenderer(this, 0, 0);
        MainPlatform.addBox(0F, 0F, 0F, 10, 1, 10);
        MainPlatform.setRotationPoint(-5F, 23F, -5F);
        MainPlatform.setTextureSize(64, 32);
        MainPlatform.mirror = true;
        setRotation(MainPlatform, 0F, 0F, 0F);
        SmallPlatform4 = new ModelRenderer(this, 0, 16);
        SmallPlatform4.addBox(0F, 0F, 0F, 4, 1, 4);
        SmallPlatform4.setRotationPoint(1F, 22F, 1F);
        SmallPlatform4.setTextureSize(64, 32);
        SmallPlatform4.mirror = true;
        setRotation(SmallPlatform4, 0F, 0F, 0F);
        SmallPlatform1 = new ModelRenderer(this, 0, 21);
        SmallPlatform1.addBox(0F, 0F, 0F, 4, 1, 4);
        SmallPlatform1.setRotationPoint(-5F, 22F, -5F);
        SmallPlatform1.setTextureSize(64, 32);
        SmallPlatform1.mirror = true;
        setRotation(SmallPlatform1, 0F, 0F, 0F);
        UpperSmallPlatform2 = new ModelRenderer(this, 0, 26);
        UpperSmallPlatform2.addBox(0F, 0F, 0F, 4, 1, 4);
        UpperSmallPlatform2.setRotationPoint(1F, 13F, -5F);
        UpperSmallPlatform2.setTextureSize(64, 32);
        UpperSmallPlatform2.mirror = true;
        setRotation(UpperSmallPlatform2, 0F, 0F, 0F);
        Pillar4 = new ModelRenderer(this, 40, 0);
        Pillar4.addBox(0F, 0F, 0F, 2, 10, 2);
        Pillar4.setRotationPoint(-5F, 13F, -1F);
        Pillar4.setTextureSize(64, 32);
        Pillar4.mirror = true;
        setRotation(Pillar4, 0F, 0F, 0F);
        Pillar1 = new ModelRenderer(this, 40, 0);
        Pillar1.addBox(0F, 0F, 0F, 2, 10, 2);
        Pillar1.setRotationPoint(-1F, 13F, -5F);
        Pillar1.setTextureSize(64, 32);
        Pillar1.mirror = true;
        setRotation(Pillar1, 0F, 0F, 0F);
        Pillar2 = new ModelRenderer(this, 40, 0);
        Pillar2.addBox(0F, 0F, 0F, 2, 10, 2);
        Pillar2.setRotationPoint(3F, 13F, -1F);
        Pillar2.setTextureSize(64, 32);
        Pillar2.mirror = true;
        setRotation(Pillar2, 0F, 0F, 0F);
        Pillar3 = new ModelRenderer(this, 40, 0);
        Pillar3.addBox(0F, 0F, 0F, 2, 10, 2);
        Pillar3.setRotationPoint(-1F, 13F, 3F);
        Pillar3.setTextureSize(64, 32);
        Pillar3.mirror = true;
        setRotation(Pillar3, 0F, 0F, 0F);
        UpperSmallPlatform1 = new ModelRenderer(this, 0, 21);
        UpperSmallPlatform1.addBox(0F, 0F, 0F, 4, 1, 4);
        UpperSmallPlatform1.setRotationPoint(-5F, 13F, -5F);
        UpperSmallPlatform1.setTextureSize(64, 32);
        UpperSmallPlatform1.mirror = true;
        setRotation(UpperSmallPlatform1, 0F, 0F, 0F);
        SmallPlatform3 = new ModelRenderer(this, 0, 26);
        SmallPlatform3.addBox(0F, 0F, 0F, 4, 1, 4);
        SmallPlatform3.setRotationPoint(1F, 22F, -5F);
        SmallPlatform3.setTextureSize(64, 32);
        SmallPlatform3.mirror = true;
        setRotation(SmallPlatform3, 0F, 0F, 0F);
        UpperSmallPlatform3 = new ModelRenderer(this, 0, 11);
        UpperSmallPlatform3.addBox(0F, 0F, 0F, 4, 1, 4);
        UpperSmallPlatform3.setRotationPoint(-5F, 13F, 1F);
        UpperSmallPlatform3.setTextureSize(64, 32);
        UpperSmallPlatform3.mirror = true;
        setRotation(UpperSmallPlatform3, 0F, 0F, 0F);
        UpperSmallPlatform4 = new ModelRenderer(this, 0, 16);
        UpperSmallPlatform4.addBox(0F, 0F, 0F, 4, 1, 4);
        UpperSmallPlatform4.setRotationPoint(1F, 13F, 1F);
        UpperSmallPlatform4.setTextureSize(64, 32);
        UpperSmallPlatform4.mirror = true;
        setRotation(UpperSmallPlatform4, 0F, 0F, 0F);
        UpperMainPlatform = new ModelRenderer(this, 0, 0);
        UpperMainPlatform.addBox(0F, 0F, 0F, 10, 1, 10);
        UpperMainPlatform.setRotationPoint(-5F, 12F, -5F);
        UpperMainPlatform.setTextureSize(64, 32);
        UpperMainPlatform.mirror = true;
        setRotation(UpperMainPlatform, 0F, 0F, 0F);
        Glass1 = new ModelRenderer(this, 16, 11);
        Glass1.addBox(0F, 0F, 0F, 4, 8, 1);
        Glass1.setRotationPoint(-5F, 14F, 4F);
        Glass1.setTextureSize(64, 32);
        Glass1.mirror = true;
        setRotation(Glass1, 0F, 0F, 0F);
        Glass2 = new ModelRenderer(this, 16, 11);
        Glass2.addBox(0F, 0F, 0F, 4, 8, 1);
        Glass2.setRotationPoint(1F, 14F, -5F);
        Glass2.setTextureSize(64, 32);
        Glass2.mirror = true;
        setRotation(Glass2, 0F, 0F, 0F);
        Glass3 = new ModelRenderer(this, 16, 11);
        Glass3.addBox(0F, 0F, 0F, 4, 8, 1);
        Glass3.setRotationPoint(-5F, 14F, -5F);
        Glass3.setTextureSize(64, 32);
        Glass3.mirror = true;
        setRotation(Glass3, 0F, 0F, 0F);
        Glass4 = new ModelRenderer(this, 16, 11);
        Glass4.addBox(0F, 0F, 0F, 4, 8, 1);
        Glass4.setRotationPoint(1F, 14F, 4F);
        Glass4.setTextureSize(64, 32);
        Glass4.mirror = true;
        setRotation(Glass4, 0F, 0F, 0F);
        Glass5 = new ModelRenderer(this, 16, 20);
        Glass5.addBox(0F, 0F, 0F, 1, 8, 3);
        Glass5.setRotationPoint(-5F, 14F, -4F);
        Glass5.setTextureSize(64, 32);
        Glass5.mirror = true;
        setRotation(Glass5, 0F, 0F, 0F);
        Glass6 = new ModelRenderer(this, 16, 20);
        Glass6.addBox(0F, 0F, 0F, 1, 8, 3);
        Glass6.setRotationPoint(4F, 14F, -4F);
        Glass6.setTextureSize(64, 32);
        Glass6.mirror = true;
        setRotation(Glass6, 0F, 0F, 0F);
        Glass7 = new ModelRenderer(this, 16, 20);
        Glass7.addBox(0F, 0F, 0F, 1, 8, 3);
        Glass7.setRotationPoint(4F, 14F, 1F);
        Glass7.setTextureSize(64, 32);
        Glass7.mirror = true;
        setRotation(Glass7, 0F, 0F, 0F);
        Glass8 = new ModelRenderer(this, 16, 20);
        Glass8.addBox(0F, 0F, 0F, 1, 8, 3);
        Glass8.setRotationPoint(-5F, 14F, 1F);
        Glass8.setTextureSize(64, 32);
        Glass8.mirror = true;
        setRotation(Glass8, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3,
                       float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        SmallPlatform2.render(f5);
        MainPlatform.render(f5);
        SmallPlatform4.render(f5);
        SmallPlatform1.render(f5);
        UpperSmallPlatform2.render(f5);
        Pillar4.render(f5);
        Pillar1.render(f5);
        Pillar2.render(f5);
        Pillar3.render(f5);
        UpperSmallPlatform1.render(f5);
        SmallPlatform3.render(f5);
        UpperSmallPlatform3.render(f5);
        UpperSmallPlatform4.render(f5);
        UpperMainPlatform.render(f5);
        Glass1.render(f5);
        Glass2.render(f5);
        Glass3.render(f5);
        Glass4.render(f5);
        Glass5.render(f5);
        Glass6.render(f5);
        Glass7.render(f5);
        Glass8.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3,
                                  float f4, float f5, Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }
}
