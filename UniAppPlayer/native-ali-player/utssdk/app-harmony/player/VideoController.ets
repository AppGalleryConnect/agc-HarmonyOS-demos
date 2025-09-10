import { AliPlayer, AliPlayerFactory, PrivateService } from '@aliyun_video_cloud/aliyun_player';
import {
  OnPremiumLicenseVerifyCallback,
  PremiumBizType
} from '@aliyun_video_cloud/aliyun_player/src/main/ets/com/aliyun/private_service/PrivateService';
import HashMap from '@ohos.util.HashMap';

class VideoController {
  private playerMap: HashMap<number, AliPlayer> = new HashMap();
  private index: number = 0;

  public createAliPlayer(context: Context, traceId: string|null){
    let premiumVerifyCallback:OnPremiumLicenseVerifyCallback = {
      onPremiumLicenseVerifyCallback: (type: PremiumBizType, isValid: boolean, errorMsg: string) => {
      }
    }

    PrivateService.setOnPremiumLicenseVerifyCallback(premiumVerifyCallback)

    let playerInstance = AliPlayerFactory.createAliPlayer(context, traceId||'');
    this.playerMap.set(this.index, playerInstance);
    return this.index++;
  }

  public getPlayer(index: number): AliPlayer | undefined {
    if (index === -1) return;
    if (this.playerMap.hasKey(index)) {
      return this.playerMap.get(index);
    }
    return;
  }

  public dispose(index: number) {
    if (this.playerMap.hasKey(index)) {
      this.playerMap.get(index).releaseAsync();
      this.playerMap.remove((index));
    }
  }
}

export default new VideoController();

