/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/apple/Downloads/AidlDemoAdvance/AidlClient/src/com/lms/aidl/ITestService.aidl
 */
package com.lms.aidl;
public interface ITestService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lms.aidl.ITestService
{
private static final java.lang.String DESCRIPTOR = "com.lms.aidl.ITestService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lms.aidl.ITestService interface,
 * generating a proxy if needed.
 */
public static com.lms.aidl.ITestService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lms.aidl.ITestService))) {
return ((com.lms.aidl.ITestService)iin);
}
return new com.lms.aidl.ITestService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getBean:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.lms.aidl.Bean> _result = this.getBean();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_addBean:
{
data.enforceInterface(DESCRIPTOR);
com.lms.aidl.Bean _arg0;
if ((0!=data.readInt())) {
_arg0 = com.lms.aidl.Bean.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.addBean(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lms.aidl.ITestService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.util.List<com.lms.aidl.Bean> getBean() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.lms.aidl.Bean> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBean, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.lms.aidl.Bean.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addBean(com.lms.aidl.Bean bean) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((bean!=null)) {
_data.writeInt(1);
bean.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_addBean, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getBean = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_addBean = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public java.util.List<com.lms.aidl.Bean> getBean() throws android.os.RemoteException;
public void addBean(com.lms.aidl.Bean bean) throws android.os.RemoteException;
}
