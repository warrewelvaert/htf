package be.thebeehive.htf.library.protocol.server;

public class ErrorServerMessage extends ServerMessage {

    private String msg;

    public ErrorServerMessage() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
