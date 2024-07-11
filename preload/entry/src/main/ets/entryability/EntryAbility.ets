import { AbilityConstant, UIAbility, Want } from '@kit.AbilityKit';
import { hilog } from '@kit.PerformanceAnalysisKit';
import { window } from '@kit.ArkUI';
import { cloudFunction } from '@kit.CloudFoundationKit';

export default class EntryAbility extends UIAbility {
  functionPreload() {
    hilog.info(0x0000, 'testTag', 'promise start 111');
    let promise = cloudFunction.call({
      name: "preload5", // 预加载调用设置参数的地方
      timeout: 3 * 1000,
      loadMode: cloudFunction.LoadMode.PRELOAD,
    });
    promise.then((data: cloudFunction.FunctionResult) => {
      hilog.info(0x0000, 'testTag', 'promise = %{public}s', JSON.stringify(data.result));
      // todo 继续处理获取到的应用数据
    }).catch((err: Error) => {
      hilog.info(0x0000, 'testTag', 'promise catch = %{public}s', err.message);
      this.functionNormal()
    })
  }

  functionNormal() {
    hilog.info(0x0000, 'testTag', 'promise start 111');
    let promise = cloudFunction.call({
      name: "preload5", // 预加载调用设置参数的地方
      timeout: 5 * 1000,
      loadMode: cloudFunction.LoadMode.NORMAL, // 不设置默认为NORMAL
    });
    promise.then((data: cloudFunction.FunctionResult) => {
      hilog.info(0x0000, 'testTag', 'promise = %{public}s', JSON.stringify(data.result));
      // todo 继续处理获取到的应用数据
    }).catch((err: Error) => {
      hilog.info(0x0000, 'testTag', 'promise catch = %{public}s', err.message);
    })
  }

  onCreate(want: Want, launchParam: AbilityConstant.LaunchParam): void {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onCreate');
    this.functionPreload(); // 应用启动通过预加载模式获取应用数据
  }

  onDestroy(): void {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onDestroy');
  }

  onWindowStageCreate(windowStage: window.WindowStage): void {
    // Main window is created, set main page for this ability
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageCreate');

    windowStage.loadContent('pages/Index', (err) => {
      if (err.code) {
        hilog.error(0x0000, 'testTag', 'Failed to load the content. Cause: %{public}s', JSON.stringify(err) ?? '');
        return;
      }
        hilog.info(0x0000, 'testTag', 'Succeeded in loading the content.');
    });
  }

  onWindowStageDestroy(): void {
      // Main window is destroyed, release UI related resources
      hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageDestroy');
  }

  onForeground(): void {
      // Ability has brought to foreground
      hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onForeground');
  }

  onBackground(): void {
      // Ability has back to background
      hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onBackground');
  }
}
