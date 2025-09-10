
/**
 * 转换时间所用常量.
 */
const TIME_UNIT: number = 60;

/**
 * 每个片段所占字符个数.
 */
const PADDING_LENGTH: number = 2;

/**
 * 占位符.
 */
const PADDING_STR: string = '0';

export class CommonUtils {
  /**
   * 计算时长文本
   * @param duration
   * @param currentDuration
   * @returns
   */
  static getDurationString(duration: number, currentDuration: number) {
    let durationString: string = CommonUtils.secondToTime(Math.floor(duration / 1000));
    let currentDurationString: string = CommonUtils.secondToTime(Math.floor(currentDuration / 1000));
    return `${currentDurationString}/${durationString}`;
  }

  /**
   * 时间格式：Hour-minute-second
   *
   * @param seconds 入参已秒为单位
   * @returns 返回一个格式化后的字符串
   */
  static secondToTime(seconds: number): string {
    let hourUnit = TIME_UNIT * TIME_UNIT;
    let hour = Math.floor(seconds / hourUnit);
    let minute = Math.floor((seconds - hour * hourUnit) / TIME_UNIT);
    let second = seconds - hour * hourUnit - minute * TIME_UNIT;
    return `${CommonUtils.padding(hour.toString())}${':'}${CommonUtils.padding(minute.toString())}${':'}${CommonUtils.padding(second.toString())}`;
  }

  /**
   * 增加占位符.
   */
  static padding(num: string): string {
    let length = PADDING_LENGTH;
    for (let len = (num.toString()).length; len < length; len = num.length) {
      num = `${PADDING_STR}${num}`;
    }
    return num;
  }
}