import AsyncStorage from "@react-native-async-storage/async-storage";
import type { AuthResponse } from "./generated/models/AuthResponse";

const ACCESS_KEY = "access_token";
const REFRESH_KEY = "refresh_token";

export async function saveAuthTokens(res: AuthResponse) {
  if (res.accessToken) await AsyncStorage.setItem(ACCESS_KEY, res.accessToken);
  if (res.refreshToken) await AsyncStorage.setItem(REFRESH_KEY, res.refreshToken);
}

export async function getAccessToken() {
  return (await AsyncStorage.getItem(ACCESS_KEY)) ?? "";
}

export async function clearAuthTokens() {
  await AsyncStorage.multiRemove(["access_token", "refresh_token"]);
}