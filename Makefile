.PHONY: automl_zero kill

START_PARALLEL_NUM := 1
END_PARALLEL_NUM :=  5

EXP_TYPE := "TWO_LAYERS_NN_REGRESSION"
EXP_NAME := "default"
METHOD_TYPE := "DEDUPLICATED_MGG_AUTO_ML_ZERO_VAG"
METHOD_NAME := "default"

automl_zero:
	@if ! tmux has-session -t automl_zero 2>/dev/null; then \
		tmux new-session -s automl_zero -d; \
	fi; \
	for i in $$(seq $(START_PARALLEL_NUM) $(END_PARALLEL_NUM)); do \
		tmux new-window -t automl_zero:$$i -n automl_zero-$$i; \
		tmux send-keys -t automl_zero:$$i "sh ./exp.sh $(EXP_TYPE) $(EXP_NAME) $(METHOD_TYPE) $(METHOD_NAME) $$i $$i" C-m; \
	done

kill:
	@if tmux has-session -t automl_zero 2>/dev/null; then \
		tmux kill-session -t automl_zero; \
		echo "Stopped all automl_zero"; \
	else \
		echo "No session named automl_zero found."; \
	fi
