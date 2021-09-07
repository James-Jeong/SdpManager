# SdpManager
SDP Management for enhanced performance
  
  
## Structure
### 1) SDP
#### 1-1) SessionDescriptionFactory
##### 1-1-1) VersionField (Mandatory)
- versionType
- version
  
##### 1-1-2) OriginField (Mandatory)
- originType
- originUserName
- originAddress
- originAddressType
- originNetworkType
- sessionId
- sessionVersion
  
##### 1-1-3) SessionField (Mandatory)
- sessionType
- sessionName
  
##### 1-1-4) ConnectionField (Optional)
- connectionType
- connectionAddress
- connectionAddressType
- connectionNetworkType
  
#### 1-2) TimeDescriptionFactory
##### 1-2-1) TimeField (Mandatory)
- timeType
- startTime
- endTime
  
  
#### 1-3) MediaDesciptionFactory
##### 1-3-1) MediaFactory
###### 1-3-1-1) ConnectionField (Optional)
- connectionType
- connectionAddress
- connectionAddressType
- connectionNetworkType
  
###### 1-3-1-2) MediaField (Mandatory)
- type
- mediaType
- mediaPort
- protocol
- mediaFormats
- portCount
  
###### 1-3-1-3) Attribute (Mandatory)
- RtpAttribute
payloadId  
customAttributeFactory  
rtpMapAttributeFactory  
fmtpAttributeFactoryList  
  
- AttributeFactory
type  
name  
payloadId  
value  
  
- RtpMapAttributeFactory
codecName  
samplingRate  
isOctetAlign  
  
- FmtpAttributeFactory
modeSet  
isOaMode  
  
  
