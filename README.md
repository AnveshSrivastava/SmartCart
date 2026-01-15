### Building the docker image

**Command**

```
docker buildx build --platform linux/amd64 -t <username>/<image_name> .
```

**Example**

```
docker buildx build --platform linux/amd64 -t aakashverma1124/threadly-user-authentication-service .
```

### Pushing the docker image

**Command**

```
docker push <username>/<image_name>
```

**Example**

```
docker push aakashverma1124/threadly-user-authentication-service
```
