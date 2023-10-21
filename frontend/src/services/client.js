import axios from "axios";

const getAuthConfig = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem("access_token")}`,
  },
});

const BASE_URL = "http://localhost:8080";

export const login = async (nameAndPassword) => {
  try {
    return await axios.post(`${BASE_URL}/auth/login`, nameAndPassword);
  } catch (e) {
    throw e;
  }
};

export const findClientByCpf = async (cpf) => {
  try {
    return await axios.get(`${BASE_URL}/client/${cpf}`, getAuthConfig());
  } catch (e) {
    throw e;
  }
};

export const getVehicleById = async (id) => {
  try {
    return await axios.get(`${BASE_URL}/vehicle/${id}`, getAuthConfig());
  } catch (e) {
    throw e;
  }
};

export const getSituations = async () => {
  try {
    return await axios.get(`${BASE_URL}/situation`, getAuthConfig());
  } catch (e) {
    throw e;
  }
};

export const getObservations = async () => {
  try {
    return await axios.get(`${BASE_URL}/observation`, getAuthConfig());
  } catch (e) {
    throw e;
  }
};

export const getSpecificServicesByModel = async (model) => {
  try {
    return await axios.get(
      `${BASE_URL}/specificService/${model}`,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const getGeneralServices = async () => {
  try {
    return await axios.get(`${BASE_URL}/generalService`, getAuthConfig());
  } catch (e) {
    throw e;
  }
};

export const saveObservation = async (nameAndSituation) => {
  try {
    return await axios.post(
      `${BASE_URL}/observation`,
      nameAndSituation,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const saveSpecificService = async (
  serviceNameAndSituationAndVehicleModel
) => {
  try {
    return await axios.post(
      `${BASE_URL}/specificService`,
      serviceNameAndSituationAndVehicleModel,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const saveGeneralService = async (serviceNameAndSituation) => {
  try {
    return await axios.post(
      `${BASE_URL}/generalService`,
      serviceNameAndSituation,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const saveOrderOfService = async (orderOfServiceRequest) => {
  const request = {
    vehicleId: orderOfServiceRequest.vehicleId,
    clientCpf: orderOfServiceRequest.clientCpf,
    observationIds: orderOfServiceRequest.observationIds,
    specificServiceIds: orderOfServiceRequest.specificServicesIds,
    generalServiceIds: orderOfServiceRequest.generalServicesIds,
  };
  try {
    return await axios.post(
      `${BASE_URL}/orderOfService`,
      request,
      getAuthConfig()
    );
  } catch (e) {
    throw e;
  }
};

export const getOrderOfServiceById = async (id) => {
  try {
    return await axios.get(`${BASE_URL}/orderOfService/${id}`, getAuthConfig());
  } catch (e) {
    throw e;
  }
};
