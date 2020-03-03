package textAnalysis.core.subst;

import textAnalysis.provider.AProvider;

public class ASimpleAnalysis implements AProvider {

	@Override
	public String getName() {
		return "A Simple Analysis";
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
