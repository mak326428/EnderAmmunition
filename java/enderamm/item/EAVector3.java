package enderamm.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class EAVector3 {
    public double x;
    public double y;
    public double z;

    public EAVector3() {
    }

    public EAVector3(double d, double d1, double d2) {
        this.x = d;
        this.y = d1;
        this.z = d2;
    }

    public EAVector3(EAVector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public EAVector3(Vec3 vec) {
        this.x = vec.xCoord;
        this.y = vec.yCoord;
        this.z = vec.zCoord;
    }

    public EAVector3 copy() {
        return new EAVector3(this);
    }

    public static EAVector3 fromEntity(Entity e) {
        return new EAVector3(e.posX, e.posY, e.posZ);
    }

    public static EAVector3 fromEntityCenter(Entity e) {
        return new EAVector3(e.posX, e.posY - e.yOffset + e.height / 2.0F, e.posZ);
    }

    public static EAVector3 fromTileEntity(TileEntity e) {
        return new EAVector3(e.xCoord, e.yCoord, e.zCoord);
    }

    public static EAVector3 fromTileEntityCenter(TileEntity e) {
        return new EAVector3(e.xCoord + 0.5D, e.yCoord + 0.5D, e.zCoord + 0.5D);
    }

    @Deprecated
    public static EAVector3 fromVec3D(Vec3 vec) {
        return new EAVector3(vec);
    }

    public EAVector3 set(double d, double d1, double d2) {
        this.x = d;
        this.y = d1;
        this.z = d2;
        return this;
    }

    public EAVector3 set(EAVector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    public double dotProduct(EAVector3 vec) {
        double d = vec.x * this.x + vec.y * this.y + vec.z * this.z;

        if ((d > 1.0D) && (d < 1.00001D))
            d = 1.0D;
        else if ((d < -1.0D) && (d > -1.00001D))
            d = -1.0D;
        return d;
    }

    public double dotProduct(double d, double d1, double d2) {
        return d * this.x + d1 * this.y + d2 * this.z;
    }

    public EAVector3 crossProduct(EAVector3 vec) {
        double d = this.y * vec.z - this.z * vec.y;
        double d1 = this.z * vec.x - this.x * vec.z;
        double d2 = this.x * vec.y - this.y * vec.x;
        this.x = d;
        this.y = d1;
        this.z = d2;
        return this;
    }

    public EAVector3 add(double d, double d1, double d2) {
        this.x += d;
        this.y += d1;
        this.z += d2;
        return this;
    }

    public EAVector3 add(EAVector3 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public EAVector3 subtract(EAVector3 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public EAVector3 multiply(double d) {
        this.x *= d;
        this.y *= d;
        this.z *= d;
        return this;
    }

    public EAVector3 multiply(EAVector3 f) {
        this.x *= f.x;
        this.y *= f.y;
        this.z *= f.z;
        return this;
    }

    public EAVector3 multiply(double fx, double fy, double fz) {
        this.x *= fx;
        this.y *= fy;
        this.z *= fz;
        return this;
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double magSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public EAVector3 normalize() {
        double d = mag();
        if (d != 0.0D) {
            multiply(1.0D / d);
        }
        return this;
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector: " + new BigDecimal(this.x, cont) + ", "
                + new BigDecimal(this.y, cont) + ", "
                + new BigDecimal(this.z, cont);
    }

    public EAVector3 perpendicular() {
        if (this.z == 0.0D) {
            return zCrossProduct();
        }

        return xCrossProduct();
    }

    public EAVector3 xCrossProduct() {
        double d = this.z;
        double d1 = -this.y;
        this.x = 0.0D;
        this.y = d;
        this.z = d1;
        return this;
    }

    public EAVector3 zCrossProduct() {
        double d = this.y;
        double d1 = -this.x;
        this.x = d;
        this.y = d1;
        this.z = 0.0D;
        return this;
    }

    public EAVector3 yCrossProduct() {
        double d = -this.z;
        double d1 = this.x;
        this.x = d;
        this.y = 0.0D;
        this.z = d1;
        return this;
    }

    public Vec3 toVec3D() {
        return Vec3.createVectorHelper(this.x, this.y, this.z);
    }

    public double angle(EAVector3 vec) {
        return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
    }

    public boolean isZero() {
        return (this.x == 0.0D) && (this.y == 0.0D) && (this.z == 0.0D);
    }

    public boolean isAxial() {
        return (this.y == 0.0D) || (this.z == 0.0D);
    }

    @SideOnly(Side.CLIENT)
    public Vector3f vector3f() {
        return new Vector3f((float) this.x, (float) this.y, (float) this.z);
    }

    @SideOnly(Side.CLIENT)
    public Vector4f vector4f() {
        return new Vector4f((float) this.x, (float) this.y, (float) this.z,
                1.0F);
    }

    public EAVector3 negate() {
        this.x = (-this.x);
        this.y = (-this.y);
        this.z = (-this.z);
        return this;
    }
}