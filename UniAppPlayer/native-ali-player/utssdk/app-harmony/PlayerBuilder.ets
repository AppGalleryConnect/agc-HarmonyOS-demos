import { defineNativeEmbed, NativeEmbedBuilderOptions } from '@dcloudio/uni-app-runtime';
import { VOD_TYPE } from './player/Constants';
import { IAuthInfo, InvokeMethod } from './player/Modals';
import { PlayerPage } from './player/Player.ets';

interface PlayerBuilderOptions extends NativeEmbedBuilderOptions {
    videoUrl: string
    invokeMethod: InvokeMethod
    authInfo: IAuthInfo
    vodType: VOD_TYPE
}

@Builder
function PlayerBuilder(options: PlayerBuilderOptions) {
    PlayerPage({
        videoUrl: options.videoUrl,
        vodType: options.vodType,
        authInfo: options.authInfo,
        invokeMethod: options.invokeMethod,
        onFullScreen: options.on?.get('onfullscreen'),
        onProgress: options.on?.get('onprogress'),
        onState: options.on?.get('onstate'),
		onBackButton: options.on?.get('onbackbutton')
    })
        .width(options.width)
        .height(options.height);
}
defineNativeEmbed('native-ali-player', {
    builder: PlayerBuilder
});
