/**
 * Copyright (c) 2017, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1)Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3)Neither the name of comdor nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package co.comdor;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.EventStream;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerChange;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerExit;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.ContainerUpdate;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.ExecState;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.ImageHistory;
import com.spotify.docker.client.messages.ImageInfo;
import com.spotify.docker.client.messages.ImageSearchResult;
import com.spotify.docker.client.messages.Info;
import com.spotify.docker.client.messages.Network;
import com.spotify.docker.client.messages.NetworkConfig;
import com.spotify.docker.client.messages.NetworkConnection;
import com.spotify.docker.client.messages.NetworkCreation;
import com.spotify.docker.client.messages.RegistryAuth;
import com.spotify.docker.client.messages.RemovedImage;
import com.spotify.docker.client.messages.ServiceCreateResponse;
import com.spotify.docker.client.messages.TopResults;
import com.spotify.docker.client.messages.Version;
import com.spotify.docker.client.messages.Volume;
import com.spotify.docker.client.messages.VolumeList;
import com.spotify.docker.client.messages.swarm.Config;
import com.spotify.docker.client.messages.swarm.ConfigCreateResponse;
import com.spotify.docker.client.messages.swarm.ConfigSpec;
import com.spotify.docker.client.messages.swarm.Node;
import com.spotify.docker.client.messages.swarm.NodeInfo;
import com.spotify.docker.client.messages.swarm.NodeSpec;
import com.spotify.docker.client.messages.swarm.Secret;
import com.spotify.docker.client.messages.swarm.SecretCreateResponse;
import com.spotify.docker.client.messages.swarm.SecretSpec;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.ServiceSpec;
import com.spotify.docker.client.messages.swarm.Swarm;
import com.spotify.docker.client.messages.swarm.SwarmInit;
import com.spotify.docker.client.messages.swarm.SwarmJoin;
import com.spotify.docker.client.messages.swarm.SwarmSpec;
import com.spotify.docker.client.messages.swarm.Task;
import com.spotify.docker.client.messages.swarm.UnlockKey;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * A docker client which is not connected, used by a docker host before
* #connect() is called.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 * @checkstyle FinalParameters (800 lines)
 * @checkstyle ParameterNumber (800 lines)
 * @checkstyle ParameterName (800 lines)
 * @checkstyle LineLength (800 lines)
 * @checkstyle ClassFanOutComplexity (800 lines)
 * @checkstyle MethodCount (800 lines)
 */
public final class Disconnected implements DockerClient {

    /**
     * Message to throw in case a method is called
     * on this disconnected DockerClient.
     */
    private static final String DISCONNECTED = "Not connected. Don't forget to "
        + "get a connected instance by calling #connect()";
    
    @Override
    public String ping() {
        throw new IllegalStateException(DISCONNECTED);
    }

    @Override
    public Version version() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public int auth(RegistryAuth ra) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Info info() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Container> listContainers(ListContainersParam... lcps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Image> listImages(ListImagesParam... lips) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ContainerInfo inspectContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ContainerCreation commitContainer(String string, String string1, String string2, ContainerConfig cc, String string3, String string4) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ImageInfo inspectImage(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<RemovedImage> removeImage(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<RemovedImage> removeImage(String string, boolean bln, boolean bln1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<ImageSearchResult> searchImages(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void load(String string, InputStream in) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void load(String string, InputStream in, ProgressHandler ph) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Set<String> load(InputStream in) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Set<String> load(InputStream in, ProgressHandler ph) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void create(String string, InputStream in) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void create(String string, InputStream in, ProgressHandler ph) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public InputStream save(String... strings) throws DockerException, InterruptedException {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public InputStream saveMultiple(String... strings) throws DockerException, InterruptedException {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public TopResults topContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public TopResults topContainer(String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void pull(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void pull(String string, ProgressHandler ph) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void pull(String string, RegistryAuth ra) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void pull(String string, RegistryAuth ra, ProgressHandler ph) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void push(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void push(String string, ProgressHandler ph) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void push(String string, RegistryAuth ra) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void push(String string, ProgressHandler ph, RegistryAuth ra) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void tag(String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void tag(String string, String string1, boolean bln) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public String build(Path path, BuildParam... bps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public String build(Path path, String string, BuildParam... bps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public String build(Path path, ProgressHandler ph, BuildParam... bps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public String build(Path path, String string, ProgressHandler ph, BuildParam... bps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public String build(Path path, String string, String string1, ProgressHandler ph, BuildParam... bps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<ImageHistory> history(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ContainerCreation createContainer(ContainerConfig cc) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ContainerCreation createContainer(ContainerConfig cc, String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void renameContainer(String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ContainerUpdate updateContainer(String string, HostConfig hc) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void startContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void stopContainer(String string, int i) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void pauseContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void unpauseContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void restartContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void restartContainer(String string, int i) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ContainerExit waitContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void killContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void killContainer(String string, Signal signal) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void removeContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void removeContainer(String string, RemoveContainerParam... rcps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void removeContainer(String string, boolean bln) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public InputStream exportContainer(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public InputStream copyContainer(String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public InputStream archiveContainer(String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void copyToContainer(Path path, String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void copyToContainer(InputStream in, String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<ContainerChange> inspectContainerChanges(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public LogStream logs(String string, LogsParam... lps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public EventStream events(EventsParam... eps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ExecCreation execCreate(String string, String[] strings, ExecCreateParam... ecps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public LogStream execStart(String string, ExecStartParameter... esps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Swarm inspectSwarm() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public String initSwarm(SwarmInit si) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void joinSwarm(SwarmJoin sj) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void leaveSwarm() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void leaveSwarm(boolean bln) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateSwarm(Long l, boolean bln, boolean bln1, boolean bln2, SwarmSpec ss) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateSwarm(Long l, boolean bln, boolean bln1, SwarmSpec ss) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateSwarm(Long l, boolean bln, SwarmSpec ss) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateSwarm(Long l, SwarmSpec ss) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public UnlockKey unlockKey() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void unlock(UnlockKey uk) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ServiceCreateResponse createService(ServiceSpec ss) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ServiceCreateResponse createService(ServiceSpec ss, RegistryAuth ra) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Service inspectService(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateService(String string, Long l, ServiceSpec ss) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateService(String string, Long l, ServiceSpec ss, RegistryAuth ra) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Service> listServices() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Service> listServices(Service.Criteria crtr) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void removeService(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public LogStream serviceLogs(String string, LogsParam... lps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Task inspectTask(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Task> listTasks() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Task> listTasks(Task.Criteria crtr) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void execResizeTty(String string, Integer intgr, Integer intgr1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ExecState execInspect(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ContainerStats stats(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void resizeTty(String string, Integer intgr, Integer intgr1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Network> listNetworks(ListNetworksParam... lnps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Network inspectNetwork(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public NetworkCreation createNetwork(NetworkConfig nc) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void removeNetwork(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void connectToNetwork(String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void connectToNetwork(String string, NetworkConnection nc) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void disconnectFromNetwork(String string, String string1) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void disconnectFromNetwork(String string, String string1, boolean bln) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void close() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public LogStream attachContainer(String string, AttachParameter... aps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public String getHost() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Volume createVolume() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Volume createVolume(Volume volume) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Volume inspectVolume(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void removeVolume(Volume volume) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void removeVolume(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public VolumeList listVolumes(ListVolumesParam... lvps) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Secret> listSecrets() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public SecretCreateResponse createSecret(SecretSpec ss) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Secret inspectSecret(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void deleteSecret(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Config> listConfigs() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Config> listConfigs(Config.Criteria crtr) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public ConfigCreateResponse createConfig(ConfigSpec cs) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public Config inspectConfig(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void deleteConfig(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateConfig(String string, Long l, ConfigSpec cs) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Node> listNodes() {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public List<Node> listNodes(Node.Criteria crtr) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public NodeInfo inspectNode(String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void updateNode(String string, Long l, NodeSpec ns) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void deleteNode(final String string) {
        throw new IllegalStateException(DISCONNECTED); 
    }

    @Override
    public void deleteNode(final String string, final boolean bln) {
        throw new IllegalStateException(DISCONNECTED); 
    }
    
}
