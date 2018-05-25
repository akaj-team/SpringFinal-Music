package android.asiantech.vn.springfinalmusic.headphone

interface IListenesHPhoneChanger {
    //state=0 phone isDisconnected,state=1 phone isConnected,state =-1 ERROR
    fun onCommand(state: Int)
}
