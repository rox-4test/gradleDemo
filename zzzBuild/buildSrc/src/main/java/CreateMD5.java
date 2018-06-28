import java.io.File;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.IsolationMode;
import org.gradle.workers.WorkerConfiguration;
import org.gradle.workers.WorkerExecutor;

class CreateMD5 extends SourceTask {
	private final WorkerExecutor workerExecutor;

	@OutputDirectory
	File destinationDir;

	@InputFiles
	FileCollection codecClasspath;

	@Inject
	public CreateMD5(WorkerExecutor workerExecutor) {
		super();
		this.workerExecutor = workerExecutor;
	}

	@TaskAction
	public void createHashes() {
		for (File sourceFile : getSource().getFiles()) {
			File md5File = new File(destinationDir, sourceFile.getName() + ".md5");
			workerExecutor.submit(GenerateMD5.class, new Action<WorkerConfiguration>() {
				@Override
				public void execute(WorkerConfiguration config) {
					config.setIsolationMode(IsolationMode.CLASSLOADER);
					config.params(sourceFile, md5File);
					config.classpath(codecClasspath);
				}
			});
		}
	}
}