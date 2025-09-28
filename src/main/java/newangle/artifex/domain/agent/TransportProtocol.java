package newangle.artifex.domain.agent;

public enum TransportProtocol {

    JSONRPC("JSONRPC"),
    GRPC("GRPC"),
    HTTP_JSON("HTTP+JSON");

    private String transport;

    TransportProtocol(String transport) {
        this.transport = transport;
    }

    public String getTransport() {
        return transport;
    }

}