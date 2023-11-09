package be.thebeehive.htf.library.protocol.server;

public class WarningServerMessage extends ServerMessage {

    private String msg;

    public WarningServerMessage() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
