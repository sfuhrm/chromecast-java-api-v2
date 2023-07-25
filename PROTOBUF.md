# Generating the protobuf file

The protobuf Java binding can be generated with the
protoc tool (Debian: `protobuf-compiler`).

The following is the command line to re-generate the
binding class:

```bash
protoc ./src/main/resources/su/litvak/justdlna/chromecast/v2/cast_channel.proto --java_out=lite:src/main/java/ 
```
