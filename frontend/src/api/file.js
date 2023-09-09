import axios from "axios";
import { request } from "./index";

export const getFiles = (path) => {
  return request.get(`/api/files`, {
    params: {
        path: path,
    },
  });
};

export const uploadFiles = async (formData) => {
  const response = await axios.post(`/api/files`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    }
  });
};