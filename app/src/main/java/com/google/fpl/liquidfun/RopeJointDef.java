/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class RopeJointDef extends JointDef {
  private transient long swigCPtr;

  protected RopeJointDef(long cPtr, boolean cMemoryOwn) {
    super(liquidfunJNI.RopeJointDef_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(RopeJointDef obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_RopeJointDef(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public void setLocalAnchorA(float x, float y) {
    liquidfunJNI.RopeJointDef_setLocalAnchorA(swigCPtr, this, x, y);
  }

  public void setLocalAnchorB(float x, float y) {
    liquidfunJNI.RopeJointDef_setLocalAnchorB(swigCPtr, this, x, y);
  }

  public void setMaxLength(float value) {
    liquidfunJNI.RopeJointDef_maxLength_set(swigCPtr, this, value);
  }

  public float getMaxLength() {
    return liquidfunJNI.RopeJointDef_maxLength_get(swigCPtr, this);
  }

  public RopeJointDef() {
    this(liquidfunJNI.new_RopeJointDef(), true);
  }

}