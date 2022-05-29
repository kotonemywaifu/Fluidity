package me.liuli.fluidity.util.move

import me.liuli.fluidity.util.mc
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sqrt


val EntityPlayerSP.serverRotationYaw: Float
    get() = if (!silentRotationYaw.isNaN()) silentRotationYaw else this.rotationYaw

val EntityPlayerSP.serverRotationPitch: Float
    get() = if (!silentRotationPitch.isNaN()) silentRotationPitch else this.rotationPitch

var silentRotationYaw = Float.NaN
var silentRotationPitch = Float.NaN
var lastReportedYaw = 0f
var lastReportedPitch = 0f

fun setServerRotation(yaw: Float, pitch: Float) {
    silentRotationYaw = yaw
    silentRotationPitch = pitch
}

fun EntityLivingBase.applyVisualYawUpdate() {
    if (!silentRotationYaw.isNaN()) {
        println("YAW${this.rotationYaw}")
        this.rotationYawHead = silentRotationYaw
        this.renderYawOffset = silentRotationYaw
    }
}

/**
 * Calculate difference between the client rotation and your entity
 *
 * @param entity your entity
 * @return difference between rotation
 */
fun getRotationDifference(entity: Entity): Double {
    val rotation = toRotation(getCenter(entity.entityBoundingBox), true)
    return getRotationDifference(rotation.first, rotation.second, mc.thePlayer.serverRotationYaw, mc.thePlayer.serverRotationPitch)
}

/**
 * Calculate difference between two rotations
 *
 * @param a rotation
 * @param b rotation
 * @return difference between rotation
 */
fun getRotationDifference(aYaw: Float, aPitch: Float, bYaw: Float, bPitch: Float): Double {
    return hypot(getAngleDifference(aYaw, bYaw).toDouble(), (aPitch - bPitch).toDouble())
}

/**
 * Get the center of a box
 *
 * @param bb your box
 * @return center of box
 */
fun getCenter(bb: AxisAlignedBB): Vec3 {
    return Vec3(
        bb.minX + (bb.maxX - bb.minX) * 0.5,
        bb.minY + (bb.maxY - bb.minY) * 0.5,
        bb.minZ + (bb.maxZ - bb.minZ) * 0.5
    )
}

/**
 * Translate vec to rotation
 *
 * @param vec     target vec
 * @param predict predict new location of your body
 * @return rotation
 */
fun toRotation(vec: Vec3, predict: Boolean): Pair<Float, Float> {
    val eyesPos = Vec3(mc.thePlayer.posX, mc.thePlayer.entityBoundingBox.minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ)
    if (predict) eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ)
    val diffX = vec.xCoord - eyesPos.xCoord
    val diffY = vec.yCoord - eyesPos.yCoord
    val diffZ = vec.zCoord - eyesPos.zCoord
    return Pair(
        MathHelper.wrapAngleTo180_float(Math.toDegrees(atan2(diffZ, diffX)).toFloat() - 90f),
        MathHelper.wrapAngleTo180_float((-Math.toDegrees(atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ)))).toFloat())
    )
}

/**
 * Calculate difference between two angle points
 *
 * @param a angle point
 * @param b angle point
 * @return difference between angle points
 */
fun getAngleDifference(a: Float, b: Float): Float {
    return ((a - b) % 360f + 540f) % 360f - 180f
}

/**
 * Limit your rotation using a turn speed
 *
 * @param currentRotation your current rotation
 * @param targetRotation your goal rotation
 * @param turnSpeed your turn speed
 * @return limited rotation
 */
fun limitAngleChange(aYaw: Float, aPitch: Float, bYaw: Float, bPitch: Float, turnSpeed: Float): Pair<Float, Float> {
    val yawDifference = getAngleDifference(bYaw, aYaw)
    val pitchDifference = getAngleDifference(bPitch, bPitch)
    return Pair(
        aYaw + if (yawDifference > turnSpeed) turnSpeed else yawDifference.coerceAtLeast(-turnSpeed),
        aPitch + if (pitchDifference > turnSpeed) turnSpeed else pitchDifference.coerceAtLeast(-turnSpeed)
    )
}