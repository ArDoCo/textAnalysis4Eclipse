package plugintest4.serviceproviders;

public interface NameServiceProvider {
	
	// Either load Execution Service Provider via Name-Matching or via Specified Class. I guess both is equally ugly. 
	// The later needs less computation.

	public String getName(); // TODO als static, nur objekt in der delegate erzeugen. 
	// Option als statische Map, mit so nem static block bef�llen falls man das braucht, wahrscheinlich auch static final
	// plugin optionen als map mit name etc zusammenbasteln f�r die attribute
	
	public String getExecutionServiceProviderName();

	public boolean isValid();
}
