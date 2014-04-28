package cluedo.common.message;

public enum CluedoMessageType {

	// Common Messages

	// Server Messages

	S_DEFINITION,

	S_STARTING,

	S_HAND_CARD,

	S_TURN_START,

	S_DISPROVE_REQ,

	// Client Messages

	C_SUGGESTION,

	C_DISPROVE,

	C_ACCUSATION,

	C_TURN_END,

}
