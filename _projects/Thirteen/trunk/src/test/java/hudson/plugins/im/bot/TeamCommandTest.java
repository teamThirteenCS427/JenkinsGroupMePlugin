package hudson.plugins.im.bot;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hudson.model.AbstractProject;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.ItemGroup;
import hudson.model.Run;
import hudson.plugins.im.Sender;
import hudson.scm.ChangeLogSet;

public class TeamCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void withoutParameters() throws IOException {
		Bot bot = mock(Bot.class);
		when(bot.getImId()).thenReturn("hudsonbot");

		TeamCommand cmd = new TeamCommand();
		JobProvider jobProvider = mock(JobProvider.class);
		AbstractProject project = mockProject(jobProvider);
		List<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		projects.add(project);
		when(jobProvider.getTopLevelJobs()).thenReturn(projects);
		cmd.setJobProvider(jobProvider);
		Sender sender = new Sender("sender");
		
		String replyString = cmd.getReply(bot, sender, new String[] { "log" });
		assertTrue(replyString != null);
	}

	@Test
	public void noJob() throws IOException {
		Bot bot = mock(Bot.class);
		when(bot.getImId()).thenReturn("hudsonbot");

		TeamCommand cmd = new TeamCommand();
		JobProvider jobProvider = mock(JobProvider.class);
		AbstractProject project = mockProject(jobProvider);
		List<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		when(jobProvider.getTopLevelJobs()).thenReturn(projects);
		cmd.setJobProvider(jobProvider);
		Sender sender = new Sender("sender");

		String replyString = cmd.getReply(bot, sender, new String[] { "log" });
		assertTrue(replyString.contains("no job found"));
	}

	@SuppressWarnings("unchecked")
	private AbstractProject<?, ?> mockProject(JobProvider jobProvider) {
		@SuppressWarnings("rawtypes")
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		FreeStyleBuild build = mock(FreeStyleBuild.class);
		Run run = mock(Run.class);
		ChangeLogSet changeSet = ChangeLogSet.createEmpty(run);

		when(parent.getFullDisplayName()).thenReturn("");
		when(project.getParent()).thenReturn(parent);
		when(build.getChangeSet()).thenReturn(changeSet);
		when(build.isBuilding()).thenReturn(false);
		when(project.getLastBuild()).thenReturn(build);
		return project;
	}
} 
