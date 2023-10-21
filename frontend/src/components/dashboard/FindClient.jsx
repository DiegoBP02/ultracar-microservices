import {
  Button,
  FormControl,
  Flex,
  Heading,
  Input,
  Stack,
  useColorModeValue,
  Box,
  FormLabel,
  Alert,
  AlertIcon,
} from "@chakra-ui/react";
import { findClientByCpf } from "../../services/client";
import * as Yup from "yup";
import { Form, Formik, useField } from "formik";
import { errorNotification } from "../../services/notification";
import { useState } from "react";
import ClientCard from "./ClientCard";

const MyTextInput = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className="text-input" {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
          <AlertIcon />
          {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

export default function FindClient() {
  const [client, setClient] = useState(null);

  return (
    <Flex
      minH={"100vh"}
      align={"center"}
      justify={"center"}
      bg={useColorModeValue("gray.50", "gray.800")}
      flexDir={"column"}
    >
      <Stack
        spacing={4}
        w={"full"}
        maxW={"md"}
        bg={useColorModeValue("white", "gray.700")}
        rounded={"xl"}
        boxShadow={"lg"}
        p={6}
        my={12}
      >
        <Heading lineHeight={1.1} fontSize={{ base: "2xl", md: "3xl" }}>
          Digite o CPF do cliente
        </Heading>
        <Formik
          validateOnMount={true}
          validationSchema={Yup.object({
            cpf: Yup.string()
              .length(11, "Cpf deve conter 11 caracteres")
              .required("Cpf é um campo obrigatório")
              .matches(/^\d+$/, "Cpf deve conter apenas números"),
          })}
          initialValues={{ cpf: "" }}
          onSubmit={({ cpf }, { setSubmitting }) => {
            setSubmitting(true);
            findClientByCpf(cpf)
              .then((res) => {
                setClient(res.data);
              })
              .catch((err) => {
                errorNotification(err.code, err.response.data.message);
              })
              .finally(() => {
                setSubmitting(false);
              });
          }}
        >
          {({ isValid, isSubmitting }) => (
            <Form>
              <Stack mt={15} spacing={15}>
                <MyTextInput
                  name={"cpf"}
                  type={"cpf"}
                  placeholder={"exemplo: 11122233344"}
                />
                <Button type={"submit"} isDisabled={!isValid || isSubmitting}>
                  Procurar
                </Button>
              </Stack>
            </Form>
          )}
        </Formik>
      </Stack>
      {client ? (
        <ClientCard
          name={client.name}
          cpf={client.cpf}
          email={client.email}
          phone={client.phone}
          address={client.address}
          vehicles={client.vehicles}
        ></ClientCard>
      ) : null}
    </Flex>
  );
}
