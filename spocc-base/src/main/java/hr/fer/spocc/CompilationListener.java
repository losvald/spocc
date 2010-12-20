package hr.fer.spocc;

import java.io.File;
import java.util.EventListener;

public interface CompilationListener extends EventListener {
	void sourceFileCompiled(File sourceFile);
	void sourceFileCompilationFailed(File sourceFile);
}
