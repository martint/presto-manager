/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teradata.prestomanager.agent;

import javax.ws.rs.core.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static javax.ws.rs.core.Response.Status;

//TODO: Add logger
public class APIFileHandler
{
    private final Path baseDir;

    public APIFileHandler(Path baseDir)
    {
        this.baseDir = requireNonNull(baseDir, "Base directory is null");
    }

    public Response getFileNameList()
    {
        List<String> fileNames = AgentFileUtils.getFileNameList(baseDir);
        String namesToReturn = fileNames.stream().collect(Collectors.joining("\r\n"));
        return Response.status(Status.OK).entity(namesToReturn).build();
    }

    public Response getFile(String path)
    {
        try {
            String fileContent = AgentFileUtils.getFile(Paths.get(baseDir.toString(), path));
            return Response.status(Status.OK).entity(fileContent).build();
        }
        catch (FileNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Response replaceFileFromURL(String path, String url)
    {
        try {
            AgentFileUtils.replaceFile(Paths.get(baseDir.toString(), path), url);
            return Response.status(Status.ACCEPTED).build();
        }
        catch (FileNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Response updateProperty(String path, String property, String value)
    {
        try {
            AgentFileUtils.updateProperty(Paths.get(baseDir.toString(), path), property, value);
            return Response.status(Status.ACCEPTED).build();
        }
        catch (FileNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Response deleteFile(String path)
    {
        try {
            AgentFileUtils.deleteFile(Paths.get(baseDir.toString(), path));
            return Response.status(Status.ACCEPTED).build();
        }
        catch (FileNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Response getFileProperty(String path, String property)
    {
        try {
            String value = AgentFileUtils.getFileProperty(Paths.get(baseDir.toString(), path), property);
            return Response.status(Status.OK).entity(value).build();
        }
        catch (NoSuchElementException | FileNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Response deletePropertyFromFile(String path, String property)
    {
        try {
            AgentFileUtils.removePropertyFromFile(Paths.get(baseDir.toString(), path), property);
            return Response.status(Status.ACCEPTED).build();
        }
        catch (NoSuchElementException | FileNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}