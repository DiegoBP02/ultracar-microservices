import "./OrderOfServicePDFGenerator.css";
import { Button } from "@progress/kendo-react-buttons";
import { PDFExport } from "@progress/kendo-react-pdf";
import { DropDownList } from "@progress/kendo-react-dropdowns";

import { useEffect, useRef, useState } from "react";
import { Center, Spinner } from "@chakra-ui/react";
import { errorNotification } from "../services/notification";
import { useParams } from "react-router-dom";
import { getOrderOfServiceById } from "../services/client";
import { useAuth } from "../context/AuthContext";

function OrderOfServicePDFGenerator() {
  const { orderOfServiceId } = useParams();
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [orderOfService, setOrderOfService] = useState(null);

  const pdfExportComponent = useRef(null);
  const layoutSelection = {
    text: "A4",
    value: "size-a4",
  };

  const handleExportWithComponent = (event) => {
    pdfExportComponent.current.save();
  };

  const fetchOrderOfService = () => {
    setLoading(true);
    getOrderOfServiceById(orderOfServiceId)
      .then((res) => {
        setOrderOfService(res.data);
      })
      .catch((err) => {
        errorNotification(err.code, err.response.data.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchOrderOfService();
  }, []);

  if (loading) {
    return (
      <Center>
        <Spinner
          thickness="4px"
          speed="0.65s"
          emptyColor="gray.200"
          color="blue.500"
          size="xl"
        />
      </Center>
    );
  }

  return (
    <div id="example">
      <div className="box wide hidden-on-narrow">
        <div className="box-col">
          <Button primary={true} onClick={handleExportWithComponent}>
            Baixar PDF
          </Button>
        </div>
      </div>
      <div className="page-container hidden-on-narrow">
        <PDFExport ref={pdfExportComponent}>
          <div className={`pdf-page ${layoutSelection.value}`}>
            <div className="inner-page">
              <div className="pdf-header">
                <span>
                  <span className="title">Auto Mecanica | Relatório</span>
                </span>
                <span className="invoice-number">
                  <span>ID do diagnóstico: {orderOfService.diagnosticId}</span>
                  <span>Funcionário: {user.name}</span>
                  <span>
                    Emissão:{" "}
                    {new Intl.DateTimeFormat("en-US", {
                      year: "numeric",
                      month: "2-digit",
                      day: "2-digit",
                      timeZone: "America/Sao_Paulo",
                    }).format(new Date(orderOfService.createdAt))}
                  </span>
                  <span>
                    Hora:{" "}
                    {new Date(orderOfService.createdAt).toLocaleTimeString(
                      "en-US",
                      {
                        hour: "2-digit",
                        minute: "2-digit",
                        second: "2-digit",
                        hour12: false,
                        timeZone: "America/Sao_Paulo",
                      }
                    )}
                  </span>
                </span>
              </div>
              <div className="container">
                <div className="for">
                  <h3>Dados do cliente</h3>
                  <p>
                    Cliente: {orderOfService.clientResponse.name}
                    <br />
                    CPF: {orderOfService.clientResponse.cpf}
                    <br />
                    E-mail: {orderOfService.clientResponse.email}
                    <br />
                    Telefone: {orderOfService.clientResponse.phone}
                    <br />
                    Endereço: {orderOfService.clientResponse.address}
                  </p>
                </div>
                <div className="for">
                  <h3>Dados do veiculo</h3>
                  <p>
                    Placa: {orderOfService.vehicleResponse.licensePlate}
                    <br />
                    Modelo: {orderOfService.vehicleResponse.model}
                    <br />
                    Ano: {orderOfService.vehicleResponse.year}
                    <br />
                    {orderOfService.vehicleResponse.accessories ? (
                      <span>
                        Acessórios:{" "}
                        {orderOfService.vehicleResponse.accessories.map(
                          (acs, index) => (
                            <span key={index}>
                              {acs}
                              {index !==
                                orderOfService.vehicleResponse.accessories
                                  .length -
                                  1 && ", "}
                            </span>
                          )
                        )}
                      </span>
                    ) : null}
                  </p>
                </div>
              </div>
              <hr />
              <br />
              {orderOfService.observations &&
                orderOfService.observations.length > 0 && (
                  <div className="container">
                    <div className="for">
                      <h3>Observações</h3>
                      <p>
                        {orderOfService.observations.map((obs, index) => (
                          <span key={index}>
                            {obs.name}
                            <br />
                          </span>
                        ))}
                      </p>
                    </div>
                    <div className="for">
                      <h3>Situação</h3>
                      <p>
                        {orderOfService.observations.map((obs, index) => (
                          <span key={index}>
                            {obs.situation}
                            <br />
                          </span>
                        ))}
                      </p>
                    </div>
                  </div>
                )}
              <hr />
              <br />
              {orderOfService.specificServices &&
                orderOfService.specificServices.length > 0 && (
                  <div className="container">
                    <div className="for">
                      <h3>Serviços Específicos</h3>
                      <p>
                        {orderOfService.specificServices.map(
                          (service, index) => (
                            <span key={index}>
                              {service.name}
                              <br />
                            </span>
                          )
                        )}
                      </p>
                    </div>
                    <div className="for">
                      <h3>Situação</h3>
                      <p>
                        {orderOfService.specificServices.map(
                          (service, index) => (
                            <span key={index}>
                              {service.situation}
                              <br />
                            </span>
                          )
                        )}
                      </p>
                    </div>
                  </div>
                )}
              <hr />
              <br />
              {orderOfService.generalServices &&
                orderOfService.generalServices.length > 0 && (
                  <div className="container">
                    <div className="for">
                      <h3>Serviços Gerais</h3>
                      <p>
                        {orderOfService.generalServices.map(
                          (service, index) => (
                            <span key={index}>
                              {service.name}
                              <br />
                            </span>
                          )
                        )}
                      </p>
                    </div>
                    <div className="for">
                      <h3>Situação</h3>
                      <p>
                        {orderOfService.generalServices.map(
                          (service, index) => (
                            <span key={index}>
                              {service.situation}
                              <br />
                            </span>
                          )
                        )}
                      </p>
                    </div>
                  </div>
                )}

              <div className="pdf-body">
                <span>
                  <p>Confirmação para Realização dos Serviços</p>
                  <hr />
                  <p>Assinatura do cliente: ___________________</p>
                  <p>Assinatura do técnico responsável: ___________________</p>
                </span>
              </div>
            </div>
          </div>
        </PDFExport>
      </div>
    </div>
  );
}

export default OrderOfServicePDFGenerator;
