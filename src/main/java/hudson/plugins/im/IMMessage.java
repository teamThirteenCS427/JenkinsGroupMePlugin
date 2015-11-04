package hudson.plugins.im;

/**
 * Represents a single message which is send in a IM protocol.
 * 
 * @author kutzi
 */
public class IMMessage {

    private final String from;
    private final String to;
    private final String body;
	private boolean authorized;

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (authorized ? 1231 : 1237);
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IMMessage other = (IMMessage) obj;
		if (authorized != other.authorized)
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	/**
     * Constructor.
     *
     * @param from The sender of the message
     * @param to The receiver of the message - this can e.g. be a 'user' or a 'chat room'
     * @param body The message body
     */
    public IMMessage(String from, String to, String body) {
        this(from, to, body, true);
    }
    
    public IMMessage(String from, String to, String body, boolean authorized) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.authorized = authorized;
    }
    
    /**
     * Return the addressee of the message.
     * The result is in a protocol specific format.
     * May be null.
     */
    public String getTo() {
        return this.to;
    }
    
    /**
     * Return the sender of the message.
     * The result is in a protocol specific format.
     */
    public String getFrom() {
        return this.from;
    }
    
    /**
     * Returns the message body in a plain-text format.
     */
    public String getBody() {
        return this.body;
    }
    
    public boolean isAuthorized() {
    	return this.authorized ;
    }

	@Override
	public String toString() {
		return "IMMessage [from=" + from + ", to=" + to + ", body=" + body + ", authorized=" + authorized + "]";
	}
    
    
}
