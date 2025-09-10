export const LOCAL_PROP_KEY_VOLUME: string = 'local_prop_key_volume';

export enum SEEK_TYPE {
  NORMAL = 16,
  ACCURATE = 1
}

export enum TIP_MODE {
  VOLUME,
  BRIGHTNESS
}

export enum VOD_TYPE {
  URL = 'url',
  STS = 'sts',
  AUTH = 'auth',
  MPS = 'mps',
  LIVE_STS = 'liveSts'
}

export enum PRIVACY {
LIMIT_SCREEN_SHOT = 'limit_screen_shot',
NONE_LIMIT_SCREEN_SHOT = 'none_limit_screen_shot'
}

export enum PLAYER_INSTANCE_TYPE {
  COMMON = 'common',
  LIST_PLAYER = 'list player'
}
