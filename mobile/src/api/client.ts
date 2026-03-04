import { Configuration } from "./generated/runtime";
import { AuthControllerApi } from "./generated/apis/AuthControllerApi";
import AsyncStorage from "@react-native-async-storage/async-storage";
import Constants from "expo-constants";

const host = Constants.expoConfig?.hostUri?.split(":")[0];

const BASE_URL = `http://${host}:8080`;

async function getToken() {
  return (await AsyncStorage.getItem("access_token")) ?? "";
}

const config = new Configuration({
  basePath: BASE_URL,
  accessToken: async () => await getToken(),
});

export const authApi = new AuthControllerApi(config);