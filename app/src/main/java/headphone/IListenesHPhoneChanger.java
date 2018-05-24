package headphone;

public interface IListenesHPhoneChanger {
    //state=0 phone isDisconnected,state=1 phone isConnected,state =-1 ERROR
    void onCommand(int state);
}
