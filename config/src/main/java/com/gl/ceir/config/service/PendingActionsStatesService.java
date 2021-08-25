package com.gl.ceir.config.service;

import com.gl.ceir.config.model.Action;
import com.gl.ceir.config.model.PendingActionStates;
import com.gl.ceir.config.model.constants.TransactionState;

public interface PendingActionsStatesService extends RestServices<PendingActionStates> {

	public boolean isStateChangeAllowed(TransactionState currentState, TransactionState nextState, Action action);
}
