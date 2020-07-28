# HMS Demos 
Demo the functionality of some of the Huawei mobile services:

- [AppGallery DRM License Check](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/appgallerykit-paydownload-sdkdownload) with a helper lib: https://github.com/abusuioc/hms-agc-drm-license-check

- [AudioKit](https://developer.huawei.com/consumer/en/hms/huawei-audiokit/) plays hi-res formats: https://forums.developer.huawei.com/forumPortal/en/topicview?tid=0201313058920520168

  

Demo the wrapper libs for some mobile services. Using these wrappers, a developer can achieve support for both Google Play Services (GMS) and HMS with minimal changes in the code:

1. GMS is already supported: using the wrappers is as easy as renaming the imports from the GMS specific to "mobileservices.*". The API defs of the wrappers follow 1:1 the GMS definitions.
2. There is no support yet for mobile services: import the wrappers and then just implement the GMS (or HMS) needed functionality calling the wrapper APIs (any GMS/HMS tutorial is good, since the wrappers follow 1:1 the API defs of GMS/HMS).

In any of the above cases, the result will be support for both GMS and HMS with the implementation details outside the main codebase.

List of demoed wrappers:

* Location - https://github.com/abusuioc/hms-gms-wrapper-location
* (... more will come ...)

