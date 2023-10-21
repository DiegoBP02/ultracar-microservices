import {
  Button,
  Heading,
  FormControl,
  GridItem,
  FormLabel,
  Select,
  Stack,
  Center,
  Spinner,
  Text,
} from "@chakra-ui/react";
import SidebarWithHeader from "../sharedLayout/SidebarWithHeader";

import { useNavigate, useParams } from "react-router-dom";
import { errorNotification } from "../../services/notification";
import {
  getGeneralServices,
  getObservations,
  getSituations,
  getSpecificServicesByModel,
  getVehicleById,
  saveObservation,
  saveGeneralService,
  saveSpecificService,
  saveOrderOfService,
} from "../../services/client";
import { useEffect, useState } from "react";

export default function OrderOfService() {
  const { vehicleId } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [err, setError] = useState("");
  const [vehicle, setVehicle] = useState(null);
  const [situations, setSituations] = useState([]);
  const [observations, setObservations] = useState([]);
  const [specificServices, setSpecificServices] = useState([]);
  const [generalServices, setGeneralServices] = useState([]);

  const uniqueObservations = [
    ...new Set(observations.map((observation) => observation.name)),
  ];
  const uniqueSpecificServices = [
    ...new Set(
      specificServices.map((specificService) => specificService.serviceName)
    ),
  ];
  const uniqueGeneralServices = [
    ...new Set(
      generalServices.map((generalService) => generalService.serviceName)
    ),
  ];

  const [selectedObservation, setSelectedObservation] = useState("");
  const [observationSituation, setObservationSituation] = useState("");
  const [observationList, setObservationList] = useState([]);

  const addObservation = () => {
    if (selectedObservation && observationSituation) {
      const observationExists = observationList.some(
        (observation) => observation.name === selectedObservation
      );

      if (!observationExists) {
        setObservationList([
          ...observationList,
          { name: selectedObservation, situation: observationSituation },
        ]);
        setSelectedObservation("");
        setObservationSituation("");
      } else {
        errorNotification("Erro", "Observação com o mesmo nome já existe");
      }
    }
  };

  const [selectedSpecificService, setSelectedSpecificService] = useState("");
  const [specificServiceSituation, setSpecificServiceSituation] = useState("");
  const [specificServicesList, setSpecificServicesList] = useState([]);

  const addSpecificService = () => {
    if (selectedSpecificService && specificServiceSituation) {
      const specificServiceExists = specificServicesList.some(
        (service) =>
          service.name === selectedSpecificService &&
          service.vehicleModel === vehicle.model
      );

      if (!specificServiceExists) {
        setSpecificServicesList([
          ...specificServicesList,
          {
            name: selectedSpecificService,
            situation: specificServiceSituation,
            vehicleModel: vehicle.model,
          },
        ]);
        setSelectedSpecificService("");
        setSpecificServiceSituation("");
      } else {
        errorNotification(
          "Erro",
          "Serviço específico com o mesmo nome já existe"
        );
      }
    }
  };

  const [selectedGeneralService, setSelectedGeneralService] = useState("");
  const [generalServiceSituation, setGeneralServiceSituation] = useState("");
  const [generalServicesList, setGeneralServicesList] = useState([]);

  const addGeneralService = () => {
    if (selectedGeneralService && generalServiceSituation) {
      const generalServiceExists = generalServicesList.some(
        (service) => service.name === selectedGeneralService
      );

      if (!generalServiceExists) {
        setGeneralServicesList([
          ...generalServicesList,
          {
            name: selectedGeneralService,
            situation: generalServiceSituation,
          },
        ]);
        setSelectedGeneralService("");
        setGeneralServiceSituation("");
      } else {
        errorNotification(
          "Erro",
          "Serviço específico com o mesmo nome já existe"
        );
      }
    }
  };

  const saveObservations = async () => {
    const ids = [];

    await Promise.all(
      observationList.map(async (observation) => {
        const existingObservation = observations.find(
          (obs) =>
            obs.name === observation.name &&
            obs.situation === observation.situation
        );

        if (existingObservation) {
          ids.push(existingObservation.id);
        } else {
          try {
            const observationRequest = {
              name: observation.name,
              situation: observation.situation,
            };
            const res = await saveObservation(observationRequest);
            ids.push(res.data.id);
          } catch (err) {
            setError(err.response.data.message);
            errorNotification(err.code, err.response.data.message);
          } finally {
            setLoading(false);
          }
        }
      })
    );

    return ids;
  };

  const saveSpecificServices = async () => {
    const ids = [];

    await Promise.all(
      specificServicesList.map(async (specificService) => {
        const existingSpecificService = specificServices.find(
          (ss) =>
            ss.serviceName === specificService.name &&
            ss.situation === specificService.situation &&
            ss.vehicleModel === specificService.vehicleModel
        );

        if (existingSpecificService) {
          ids.push(existingSpecificService.id);
        } else {
          try {
            const specificServiceRequest = {
              serviceName: specificService.name,
              situation: specificService.situation,
              vehicleModel: specificService.vehicleModel,
            };
            const res = await saveSpecificService(specificServiceRequest);
            ids.push(res.data.id);
          } catch (err) {
            setError(err.response.data.message);
            errorNotification(err.code, err.response.data.message);
          } finally {
            setLoading(false);
          }
        }
      })
    );

    return ids;
  };

  const saveGeneralServices = async () => {
    const ids = [];

    await Promise.all(
      generalServicesList.map(async (generalService) => {
        const existingGeneralService = generalServices.find(
          (gs) =>
            gs.serviceName === generalService.name &&
            gs.situation === generalService.situation
        );

        if (existingGeneralService) {
          ids.push(existingGeneralService.id);
        } else {
          try {
            const generalServiceRequest = {
              serviceName: generalService.name,
              situation: generalService.situation,
            };
            const res = await saveGeneralService(generalServiceRequest);
            ids.push(res.data.id);
          } catch (err) {
            setError(err.response.data.message);
            errorNotification(err.code, err.response.data.message);
          } finally {
            setLoading(false);
          }
        }
      })
    );

    return ids;
  };

  const fetchVehicle = () => {
    setLoading(true);
    getVehicleById(vehicleId)
      .then((res) => {
        setVehicle(res.data);
      })
      .catch((err) => {
        setError(err.response.data.message);
        errorNotification(err.code, err.response.data.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchSituations = () => {
    setLoading(true);
    getSituations()
      .then((res) => {
        setSituations(res.data);
      })
      .catch((err) => {
        setError(err.response.data.message);
        errorNotification(err.code, err.response.data.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchObservations = () => {
    setLoading(true);
    getObservations()
      .then((res) => {
        setObservations(res.data);
      })
      .catch((err) => {
        setError(err.response.data.message);
        errorNotification(err.code, err.response.data.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchSpecificServices = () => {
    setLoading(true);
    getSpecificServicesByModel(vehicle.model)
      .then((res) => {
        setSpecificServices(res.data);
      })
      .catch((err) => {
        setError(err.response.data.message);
        errorNotification(err.code, err.response.data.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchGeneralServices = () => {
    setLoading(true);
    getGeneralServices()
      .then((res) => {
        setGeneralServices(res.data);
      })
      .catch((err) => {
        setError(err.response.data.message);
        errorNotification(err.code, err.response.data.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const createOrderOfService = async () => {
    const clientCpf = vehicle.clientCpf;
    const observationIds = await saveObservations();
    const specificServicesIds = await saveSpecificServices();
    const generalServicesIds = await saveGeneralServices();
    const request = {
      vehicleId,
      clientCpf,
      observationIds,
      specificServicesIds,
      generalServicesIds,
    };

    await Promise.resolve(saveOrderOfService(request))
      .then((res) => navigate(`/generatePdf/${res.data.id}`))
      .catch((err) => setError(err.response.data.message));
  };

  useEffect(() => {
    fetchVehicle();
    fetchSituations();
    fetchObservations();
    fetchGeneralServices();
  }, []);

  useEffect(() => {
    if (vehicle) {
      fetchSpecificServices();
    }
  }, [vehicle]);

  if (loading) {
    return (
      <SidebarWithHeader>
        <Spinner
          thickness="4px"
          speed="0.65s"
          emptyColor="gray.200"
          color="blue.500"
          size="xl"
        />
      </SidebarWithHeader>
    );
  }

  if (err) {
    return (
      <SidebarWithHeader>
        <Text mt={5}>Um erro ocorreu: {err}</Text>
      </SidebarWithHeader>
    );
  }

  return (
    <>
      <SidebarWithHeader>
        <Heading w="100%" textAlign={"center"} fontWeight="normal" mb="2%">
          Ordem de serviço
        </Heading>
        <Center py={6}>
          <Stack
            spacing={4}
            w={"full"}
            maxW={"md"}
            rounded={"xl"}
            boxShadow={"lg"}
            p={6}
            my={12}
          >
            <FormControl as={GridItem} colSpan={[6, 3]}>
              <FormLabel htmlFor="country" fontSize="sm" fontWeight="md">
                Observações
              </FormLabel>
              <Select
                id="country"
                name="country"
                autoComplete="country"
                placeholder="Selecione uma opção"
                focusBorderColor="brand.400"
                shadow="sm"
                size="sm"
                w="full"
                rounded="md"
                value={selectedObservation}
                onChange={(e) => setSelectedObservation(e.target.value)}
              >
                {uniqueObservations.map((observation, index) => (
                  <option key={index}>{observation}</option>
                ))}
              </Select>
              <Select
                id="country"
                name="country"
                autoComplete="country"
                placeholder="Selecione uma opção"
                focusBorderColor="brand.400"
                shadow="sm"
                size="sm"
                w="full"
                rounded="md"
                mt={2}
                value={observationSituation}
                onChange={(e) => setObservationSituation(e.target.value)}
              >
                {situations.map((situation, index) => (
                  <option key={index}>{situation}</option>
                ))}
              </Select>
              <Center mt={6}>
                <Button
                  bg={"red.400"}
                  color={"white"}
                  rounded={"full"}
                  _hover={{
                    transform: "translateY(-2px)",
                    boxShadow: "lg",
                  }}
                  _focus={{
                    bg: "green.500",
                  }}
                  onClick={addObservation}
                >
                  Adicionar observação
                </Button>
              </Center>
            </FormControl>
          </Stack>
          <Stack
            spacing={4}
            w={"full"}
            maxW={"md"}
            rounded={"xl"}
            boxShadow={"lg"}
            p={6}
            my={12}
          >
            <FormControl as={GridItem} colSpan={[6, 3]}>
              <FormLabel htmlFor="country" fontSize="sm" fontWeight="md">
                Serviços específicos
              </FormLabel>
              <Select
                id="country"
                name="country"
                autoComplete="country"
                placeholder="Selecione uma opção"
                focusBorderColor="brand.400"
                shadow="sm"
                size="sm"
                w="full"
                rounded="md"
                value={selectedSpecificService}
                onChange={(e) => setSelectedSpecificService(e.target.value)}
              >
                {uniqueSpecificServices.map((specificService, index) => (
                  <option key={index}>{specificService}</option>
                ))}
              </Select>
              <Select
                id="country"
                name="country"
                autoComplete="country"
                placeholder="Select option"
                focusBorderColor="brand.400"
                shadow="sm"
                size="sm"
                w="full"
                rounded="md"
                mt={2}
                value={specificServiceSituation}
                onChange={(e) => setSpecificServiceSituation(e.target.value)}
              >
                {situations.map((situation, index) => (
                  <option key={index}>{situation}</option>
                ))}
              </Select>{" "}
              <Center mt={6}>
                <Button
                  bg={"red.400"}
                  color={"white"}
                  rounded={"full"}
                  _hover={{
                    transform: "translateY(-2px)",
                    boxShadow: "lg",
                  }}
                  _focus={{
                    bg: "green.500",
                  }}
                  onClick={addSpecificService}
                >
                  Adicionar serviço específico
                </Button>
              </Center>
            </FormControl>
          </Stack>
          <Stack
            spacing={4}
            w={"full"}
            maxW={"md"}
            rounded={"xl"}
            boxShadow={"lg"}
            p={6}
            my={12}
          >
            <FormControl as={GridItem} colSpan={[6, 3]}>
              <FormLabel htmlFor="country" fontSize="sm" fontWeight="md">
                Serviços gerais
              </FormLabel>
              <Select
                id="country"
                name="country"
                autoComplete="country"
                placeholder="Select option"
                focusBorderColor="brand.400"
                shadow="sm"
                size="sm"
                w="full"
                rounded="md"
                value={selectedGeneralService}
                onChange={(e) => setSelectedGeneralService(e.target.value)}
              >
                {uniqueGeneralServices.map((generalService, index) => (
                  <option key={index}>{generalService}</option>
                ))}
              </Select>
              <Select
                id="country"
                name="country"
                autoComplete="country"
                placeholder="Select option"
                focusBorderColor="brand.400"
                shadow="sm"
                size="sm"
                w="full"
                rounded="md"
                mt={2}
                value={generalServiceSituation}
                onChange={(e) => setGeneralServiceSituation(e.target.value)}
              >
                {situations.map((situation, index) => (
                  <option key={index}>{situation}</option>
                ))}
              </Select>{" "}
              <Center mt={6}>
                <Button
                  bg={"red.400"}
                  color={"white"}
                  rounded={"full"}
                  _hover={{
                    transform: "translateY(-2px)",
                    boxShadow: "lg",
                  }}
                  _focus={{
                    bg: "green.500",
                  }}
                  onClick={addGeneralService}
                >
                  Adicionar serviço geral
                </Button>
              </Center>
            </FormControl>
          </Stack>
        </Center>
        <Center py={6}>
          <Stack
            spacing={4}
            w={"full"}
            maxW={"md"}
            rounded={"xl"}
            boxShadow={"lg"}
            p={6}
            my={12}
          >
            <Text>Observações</Text>
            {observationList.map((observation, index) => (
              <Text key={index}>
                {observation.name} : {observation.situation}
              </Text>
            ))}
          </Stack>
          <Stack
            spacing={4}
            w={"full"}
            maxW={"md"}
            rounded={"xl"}
            boxShadow={"lg"}
            p={6}
            my={12}
          >
            <Text>Seviços específicos</Text>
            {specificServicesList.map((specificService, index) => (
              <Text key={index}>
                {specificService.name} : {specificService.situation}
              </Text>
            ))}
          </Stack>
          <Stack
            spacing={4}
            w={"full"}
            maxW={"md"}
            rounded={"xl"}
            boxShadow={"lg"}
            p={6}
            my={12}
          >
            <Text>Serviços gerais</Text>
            {generalServicesList.map((generalService, index) => (
              <Text key={index}>
                {generalService.name} : {generalService.situation}
              </Text>
            ))}
          </Stack>
        </Center>
        <Center>
          <Button
            bg={"red.400"}
            color={"white"}
            rounded={"full"}
            _hover={{
              transform: "translateY(-2px)",
              boxShadow: "lg",
            }}
            _focus={{
              bg: "green.500",
            }}
            onClick={createOrderOfService}
          >
            Criar ordem de serviço
          </Button>
        </Center>
      </SidebarWithHeader>
    </>
  );
}
