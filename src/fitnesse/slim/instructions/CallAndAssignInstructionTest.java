package fitnesse.slim.instructions;

import java.util.List;

import fitnesse.slim.NameTranslator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CallAndAssignInstructionTest {
  private static final String RESULT = "result";

  private CallAndAssignInstruction.CallAndAssignExecutor executor;
  private NameTranslator nameTranslator;

  @Before
  public void setUp() throws Exception {
    executor = mock(CallAndAssignInstruction.CallAndAssignExecutor.class);
    nameTranslator = mock(NameTranslator.class);

    when(executor.callAndAssign(anyString(), anyString(), anyString(), anyVararg())).thenReturn(RESULT);
    when(nameTranslator.translate(anyString())).thenAnswer(returnsFirstArg());
  }

  @Test
  public void shouldTranslateMethodNameOnConstruction() {
    new CallAndAssignInstruction("id_1", "symbol", "instance", "method", new Object[] {"arg1", "arg2"},
        nameTranslator);

    verify(nameTranslator, times(1)).translate("method");
  }

  @Test
  public void shouldCallExecutorOnExecution() throws Exception {
    CallAndAssignInstruction instruction = new CallAndAssignInstruction("id_1", "symbol", "instance", "method",
        new Object[] {"arg1", "arg2"}, nameTranslator);

    instruction.execute(executor);

    verify(executor, times(1)).callAndAssign("symbol", "instance", "method", "arg1", "arg2");
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldReturnExecutionResults() {
    CallAndAssignInstruction instruction = new CallAndAssignInstruction("id_1", "symbol", "instance", "method",
        new Object[] {"arg1", "arg2"}, nameTranslator);

    List<Object> results = (List<Object>) instruction.execute(executor);

    assertEquals("id_1", results.get(0));
    assertEquals(RESULT, results.get(1));
  }
}
