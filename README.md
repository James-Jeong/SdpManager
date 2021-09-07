# SdpManager
SDP Management for enhanced performance
  
  
## Structure
### 1) SDP
#### 1-1) SessionDescriptionFactory
##### 1-1-1) VersionField (Mandatory)
- versionType : char, 'v'
- version : int, 0
  
##### 1-1-2) OriginField (Mandatory)
- originType : char, 'o'
- originUserName : String
- originAddress : String
- originAddressType : String
- originNetworkType : String
- sessionId : long
- sessionVersion : long
  
##### 1-1-3) SessionField (Mandatory)
- sessionType : char, 's'
- sessionName : String
  
##### 1-1-4) ConnectionField (Optional)
- connectionType : char, 'c'
- connectionAddress : String
- connectionAddressType : String
- connectionNetworkType : String
  
#### 1-2) TimeDescriptionFactory
##### 1-2-1) TimeField (Mandatory)
- timeType : char, 't'
- startTime : String
- endTime : String
  
  
#### 1-3) MediaDesciptionFactory
##### 1-3-1) MediaFactory
###### 1-3-1-1) ConnectionField (Optional)
- connectionType : char, 'c'
- connectionAddress : String
- connectionAddressType : String
- connectionNetworkType : String
  
###### 1-3-1-2) MediaField (Mandatory)
- type : char, 'm'
- mediaType : String (audio, video, application, text, etc.)
- mediaPort : int
- protocol : String
- mediaFormats : List<String>
- portCount : int
  
###### 1-3-1-3) Attribute (Mandatory)
- RtpAttribute  
payloadId : String  
customAttributeFactory : AttributeFactory  
rtpMapAttributeFactory : RtpMapAttributeFactory  
fmtpAttributeFactoryList : List<FmtpAttributeFactory>  
  
- AttributeFactory  
type : char, 'a'  
name : String  
payloadId : String  
value : String  
  
- RtpMapAttributeFactory  
codecName : String  
samplingRate : String  
isOctetAlign : boolean  
  
- FmtpAttributeFactory  
modeSet : int  
isOaMode : boolean  
  
  
## Example
v=0  
o=- 0 0 IN IP4 127.0.0.1  
s=-  
c=IN IP4 127.0.0.1  
t=0 0  
m=audio 10000 RTP/AVP 97 98 101 102  
a=rtpmap:97 AMR/8000  
a=fmtp:97 mode-set=5; octet-align=1  
a=rtpmap:98 AMR-WB/16000  
a=fmtp:98 mode-set=6; octet-align=1  
a=rtpmap:101 telephone-event/8000  
a=fmtp:101 0-16  
a=rtpmap:102 telephone-event/16000  
a=fmtp:102 0-16  
  
