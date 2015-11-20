package hudson.plugins.im.bot;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import hudson.model.AbstractProject;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.plugins.im.Sender;
import hudson.scm.ChangeLogSet;

public class ChangesCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testChangesCommandWithoutParameters() throws IOException {
		Bot bot = mock(Bot.class);
        when(bot.getImId()).thenReturn("hudsonbot");
        
        ChangesCommand cmd = new ChangesCommand();
//        String projectName = "project name with spaces";
//        AbstractProject project = mock(AbstractProject.class);
        JobProvider jobProvider = mock(JobProvider.class);
        AbstractProject project = mockProject(jobProvider);
        List<AbstractProject<?,?>> projects = new ArrayList<AbstractProject<?,?>>();
        projects.add(project);
        when(jobProvider.getTopLevelJobs()).thenReturn(projects);
        cmd.setJobProvider(jobProvider);
        
        Sender sender = new Sender("sender");
        
        String replyString = cmd.getReply(bot, sender, new String[]{ "changes"});
//        assertTrue(replyString.startsWith("changes of all projects:"));
        assertEquals("", replyString);
	}
	
	@SuppressWarnings("unchecked")
    private AbstractProject<?, ?> mockProject(JobProvider jobProvider) {
        @SuppressWarnings("rawtypes")
        AbstractProject project = mock(FreeStyleProject.class);
        ItemGroup parent = mock(ItemGroup.class);
        FreeStyleBuild build = mock(FreeStyleBuild.class);
		when(build.getUrl()).thenReturn("job/foo/32/");
		when(parent.getFullDisplayName()).thenReturn("");
        when(jobProvider.getJobByNameOrDisplayName(Mockito.anyString())).thenReturn(project);
        when(jobProvider.getJobByNameOrDisplayName(Matchers.anyString())).thenReturn(project);
        when(project.hasPermission(Item.BUILD)).thenReturn(Boolean.TRUE);
        when(project.isBuildable()).thenReturn(true);
        when(project.getParent()).thenReturn(parent);
        when(project.getFullDisplayName()).thenReturn("fsProject");
        when(project.getLastBuild()).thenReturn(build);
        ChangeLogSet changeSet = mock(ChangeLogSet.class);
        when(build.getChangeSet()).thenReturn(changeSet);
        when(changeSet.getItems()).thenReturn(new Object[0]);
        return project; 
    }

}
