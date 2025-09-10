import { AliPlayer } from "@aliyun_video_cloud/aliyun_player"

export interface InvokeMethod {
  method: string
  versionId: number
  params: Record<string, string>
}

export interface ProgressCallback {
  detail: ProgressCallbackDetail
}

export interface ProgressCallbackDetail {
  value: number
}

export interface StateCallback {
  detail: StateCallbackDetail
}

export interface StateCallbackDetail {
  value: number
}

export interface VideoControlsOptions {
  player?: AliPlayer
}

export interface IAuthInfo {
  videoId: string
  playAuth: string
}
