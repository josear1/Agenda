package DBManager;

public class AutoCommitDisabledException extends Exception
{
	private static final long serialVersionUID = 1L;

	public AutoCommitDisabledException()
	{
		super("Se debe poner el autocommit en false para poder utilizar éste método.");
	}
}
